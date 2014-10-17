package com.flightcenter.demo.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.DAO.FlightRecord;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

/**
 * This provides flight information from a flat text file
 * 
 * @author kedaly7@gmail.com
 *
 */
public class FileProvider implements PriceProvider
{
	/**
	 * Where is the file
	 */
	private final String fileURI;
	/**
	 * How is it delimited
	 */
	private final String delimiter;
	/**
	 * Name of the provider
	 */
	private final String name;
	/**
	 * The format of the time
	 */
	private final String timeFormat;

	private List<FlightRecord> records = null;

	public FileProvider( final String name, final String fileURI, final String delimiter, final String timeFormat )
			throws IOException
	{
		this.name = name;
		this.fileURI = fileURI;
		this.delimiter = delimiter;
		this.timeFormat = timeFormat;

		init();
	}

	/**
	 * Read in the file
	 * 
	 * @throws IOException
	 */
	private void init() throws IOException
	{

		// TODO: Since this is a proof of concept piece, in a real production system, throwing an exception
		// may not be the right thing to do, we might consider refactoring to have a loadSuccessFull() method
		// in the PriceProvider interface and have the consumer validate and then raise the appropriate error

		// for testing let's get the directory for the databse from an environment varable
		String dir = System.getenv( "FLIGHT_DATA" );

		String dataPath = null;

		if( dir == null )
		{
			dataPath = fileURI;
		}
		else
		{
			dataPath = dir + "/" + fileURI;
		}

		File recordFile = new File( dataPath );
		if( recordFile.exists() )
		{

			// autoclose block
			try (BufferedReader reader =
					new BufferedReader( new InputStreamReader( new FileInputStream( recordFile ) ) ))
			{

				String line = null;

				// set up the date formater
				DateTimeFormatter parser = DateTimeFormat.forPattern( timeFormat );

				// set up the number parser
				DecimalFormat decFormatter = new DecimalFormat();
				decFormatter.setParseBigDecimal( true );

				// set will eliminate duplicates
				Set<FlightRecord> records = Sets.newTreeSet();
				while( (line = reader.readLine()) != null )
				{
					// let's parse out the thing now by tokenizing
					final String[] parsed = line.split( Pattern.quote( delimiter ) );

					// let's get the dates parsed out
					final DateTime originTime = parser.parseDateTime( parsed[1] );
					final DateTime destTime = parser.parseDateTime( parsed[3] );

					// let's parse the price remove the $ sign
					BigDecimal price = (BigDecimal)decFormatter.parse( parsed[4].replace( "$", "" ) );

					FlightRecord record = new FlightRecord( parsed[0], originTime, parsed[2], destTime, price );

					records.add( record );
				}

				// convert the set into an immutable list for thread safety
				ImmutableList.Builder<FlightRecord> builder = ImmutableList.builder();
				this.records = builder.addAll( records ).build();

			}
			catch( ParseException e )
			{
				throw new IOException( "File format is incorrect" );

			}

		}
		else
		{
			throw new IOException( "File " + fileURI + " does not exists" );
		}
	}

	@Override
	public Iterator<FlightRecord> iterator()
	{
		return records.iterator();
	}

	@Override
	public String getName()
	{
		return name;

	}

	@Override
	public int getSize()
	{
		return this.records.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.flightcenter.demo.PriceProvider#searchByRoute(java.lang.String, java.lang.String)
	 */
	@Override
	public List<FlightRecord> searchByRoute( final String orgin, final String destination )
	{
		ImmutableList.Builder<FlightRecord> builder = ImmutableList.builder();

		// brute force search
		for( FlightRecord record : this.records )
		{
			if( (record.origin.equals( orgin )) && (record.destination.equals( destination )) )
			{
				builder.add( record );
			}
		}
		return builder.build();

	}

	@Override
	public void close() throws Exception
	{
		// there's nothing to close here
	}

}
