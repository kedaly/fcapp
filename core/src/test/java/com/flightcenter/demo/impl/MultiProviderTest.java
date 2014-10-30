package com.flightcenter.demo.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.DAO.FlightRecord;
import com.google.common.collect.Sets;

public class MultiProviderTest
{
	public static final String ORGIN = "YYZ";
	public static final String DEST = "YYC";
	public static final int NUM_RESULTS = 3;

	@Test
	public void test() throws IOException
	{
		// let's run a simple test
		Set<PriceProvider> providers = Sets.newHashSet();
		providers.add( new Provider1() );
		providers.add( new Provider2() );
		providers.add( new Provider3() );

		MultiProvider provider = null;

		try
		{

			provider = new MultiProvider( providers );
			List<FlightRecord> results = provider.searchByRoute( ORGIN, DEST );

			for( FlightRecord rec : results )
			{
				System.out.println( rec.getTextRepresentation() );
			}

			assertEquals( NUM_RESULTS, results.size() );
			// TODO : More tests
		}
		finally
		{
			try
			{
				provider.close();
			}
			catch( Exception e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
