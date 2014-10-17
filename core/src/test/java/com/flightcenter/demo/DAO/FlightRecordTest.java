package com.flightcenter.demo.DAO;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Test the FlightRecord DAO
 *  
 * @author kedaly7@gmail.com
 *
 */
public class FlightRecordTest
{
	public static final String ORGIN = "AAA";
	public static final String DEST = "BBB";
	public static final DateTime DEST_DATE = new DateTime(2000,10,30,11,40);
	public static final DateTime ORGIN_DATE = new DateTime(2001,11,30,11,40);
	public static final BigDecimal PRICE = new BigDecimal( 100.23 );
	
	
	/**
	 * All we are really testing is the constructor of the DAO Object to make sure
	 * we put stuff in the right place
	 */
	@Test
	public void testConstructor()
	{
		FlightRecord record = new FlightRecord(ORGIN,ORGIN_DATE,DEST,DEST_DATE,PRICE);
		
		//let's check the record
		assertEquals( ORGIN, record.origin );
		assertEquals( ORGIN_DATE, record.orginTime );
		assertEquals( DEST, record.destination );
		assertEquals(DEST_DATE, record.destTime);
		assertEquals(PRICE, record.price);
	
	}
	
	@Test
	public void testEqualsContract() {
		EqualsVerifier.forClass( FlightRecord.class ).verify();
	}

}
