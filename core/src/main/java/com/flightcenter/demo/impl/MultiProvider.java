package com.flightcenter.demo.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.DAO.FlightRecord;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Provides a price provider that can search many providers simultaneously
 * 
 * @author kedaly7@gmail.com
 *
 */
public class MultiProvider implements PriceProvider
{
	private final String NAME = "MULTI";
	private final ExecutorService service;
	private final Set<PriceProvider> providers;

	/**
	 * Provide some providers
	 * 
	 * @param providers
	 */
	public MultiProvider( Set<PriceProvider> providers )
	{
		// let's copy everything into an immutableset for thread safety
		ImmutableSet.Builder<PriceProvider> builder = ImmutableSet.builder();
		this.providers = builder.addAll( providers ).build();

		// executor service is bounded to prevent overflow of tasks
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>( 200 );

		// run 4 threads per core
		int threads = Runtime.getRuntime().availableProcessors() * 4;
		service = new ThreadPoolExecutor( threads, threads, 1, TimeUnit.DAYS, queue );
	}

	@Override
	public Iterator<FlightRecord> iterator()
	{
		// let's just combine into one big set / not scalable but for this demo fine
		ImmutableList.Builder<FlightRecord> builder = ImmutableList.builder();

		for( PriceProvider provider : providers )
		{
			builder.addAll( provider );
		}

		return builder.build().iterator();

	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public int getSize()
	{
		int size = 0;
		for( PriceProvider provider : providers )
		{
			size = size + provider.getSize();
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flightcenter.demo.PriceProvider#searchByRoute(java.lang.String, java.lang.String)
	 */
	@Override
	public List<FlightRecord> searchByRoute( String orgin, String destination )
	{
		// let's create the result set
		Set<FlightRecord> results = Sets.newConcurrentHashSet();

		// let's wait until everything is done
		CountDownLatch latch = new CountDownLatch( providers.size() );

		// queue up a job for each provider
		for( PriceProvider provider : providers )
		{
			RunLookup lookup = new RunLookup( orgin, destination, provider, results, latch );
			service.execute( lookup );
		}

		// TODO: await for finish in production / we might have a timeout
		try
		{
			latch.await();
		}
		catch( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// let's add sort
		List<FlightRecord> records = Lists.newArrayList( results );

		Collections.sort( records );

		// return an unmodifiable
		return Collections.unmodifiableList( records );

	}

	/**
	 * Worker task to retrieve the data
	 * 
	 * @author kedaly7@gmail.com
	 *
	 */
	private static class RunLookup implements Runnable
	{

		private final Set<FlightRecord> found;
		private final String orgin;
		private final String destination;
		private final CountDownLatch latch;
		private final PriceProvider provider;

		public RunLookup( final String orgin, final String destination, final PriceProvider provider,
				final Set<FlightRecord> found, final CountDownLatch latch )
		{
			this.destination = destination;
			this.orgin = orgin;
			this.provider = provider;
			this.latch = latch;
			this.found = found;
		}

		@Override
		public void run()
		{
			try
			{
				found.addAll( provider.searchByRoute( orgin, destination ) );
			}
			finally
			{
				// make sure we release the latch
				latch.countDown();
			}
		}

	}

	@Override
	public void close() throws Exception
	{
		service.shutdown();

	}
}
