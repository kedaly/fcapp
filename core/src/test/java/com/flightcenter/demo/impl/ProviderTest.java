package com.flightcenter.demo.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.DAO.FlightRecord;

public class ProviderTest
{
	// testing variables from reading the files
	private final static BigDecimal PROVIDER_ONE_FIRST = new BigDecimal( "151.00" );
	private final static BigDecimal PROVIDER_ONE_LAST = new BigDecimal( "1093.00" );
	private final static int PROVIDER_ONE_COUNT = 8;
	private final static BigDecimal PROVIDER_ONE_MIAORD_P1 = new BigDecimal( "422.00" );
	private final static BigDecimal PROVIDER_ONE_MIAORD_P2 = new BigDecimal( "532.00" );
	private final static String ORGIN = "MIA";
	private final static String DEST = "ORD";
	private final static int RESULTS = 2;

	private final static BigDecimal PROVIDER_TWO_FIRST = new BigDecimal( "480.00" );
	private final static BigDecimal PROVIDER_TWO_LAST = new BigDecimal( "1243.00" );
	private final static int PROVIDER_TWO_COUNT = 13;

	private final static BigDecimal PROVIDER_THREE_FIRST = new BigDecimal( "201.00" );
	private final static BigDecimal PROVIDER_THREE_LAST = new BigDecimal( "1616.00" );
	private final static int PROVIDER_THREE_COUNT = 14;

	@Test
	public void test() throws IOException
	{
		final PriceProvider provider1 = new Provider1();
		testProvider( provider1, PROVIDER_ONE_FIRST, PROVIDER_ONE_LAST, PROVIDER_ONE_COUNT );

		final PriceProvider provider2 = new Provider2();
		testProvider( provider2, PROVIDER_TWO_FIRST, PROVIDER_TWO_LAST, PROVIDER_TWO_COUNT );

		final PriceProvider provider3 = new Provider3();
		testProvider( provider3, PROVIDER_THREE_FIRST, PROVIDER_THREE_LAST, PROVIDER_THREE_COUNT );

		// let's test the lookup
		List<FlightRecord> found = provider1.searchByRoute( ORGIN, DEST );

		assertEquals( RESULTS, found.size() );

		// check the prices to see if we got everything right
		assertEquals( PROVIDER_ONE_MIAORD_P1, found.get( 0 ).price );
		assertEquals( PROVIDER_ONE_MIAORD_P2, found.get( 1 ).price );

	}

	// do some tests on a provider to confirm the values in the files were read
	private void testProvider( final PriceProvider price,
			BigDecimal firstPrice,
			BigDecimal lastPrice,
			int size )
	{

		// check that we read all the records
		assertEquals( size, price.getSize() );

		// let's check the first and last is correct
		FlightRecord first = null;
		FlightRecord last = null;

		// iterate through the records to get first and last
		for( FlightRecord rec : price )
		{
			if( first == null )
			{
				first = rec;
			}
			last = rec;
		}

		// let's check
		assertEquals( firstPrice, first.price );
		assertEquals( lastPrice, last.price );

		// TODO: In a production system we might test some more data to make sure the provider is parsing correctly

	}

}
