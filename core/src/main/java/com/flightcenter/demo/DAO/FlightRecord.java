package com.flightcenter.demo.DAO;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Data class to represent a flight..
 * This is immutable since all of the variables are finals and cannot be changed.
 * 
 * @author kdaly7@gmail.com
 *
 */
public final class FlightRecord implements Comparable<FlightRecord>
{
	private static final String OUTPUT_TEMPLATE = "{%s} --> {%s} ({%s} --> {%s}) - {$%s}";

	/**
	 * The orgin of the flight
	 */
	public final String origin;

	/**
	 * What time does the flight leave
	 */
	public final DateTime orginTime;

	/**
	 * Where is the flight going
	 */
	public final String destination;

	/**
	 * What time are we arriving
	 */
	public final DateTime destTime;

	/**
	 * What is the price, we use BigDecimal to avoid accuracy problems with
	 * currency as float and double can have issues with accuracy with currency
	 */
	public final BigDecimal price;

	public FlightRecord( final String orgin, final DateTime orginTime, final String destination,
			final DateTime destTime, final BigDecimal price )
	{
		this.origin = orgin;
		this.orginTime = orginTime;
		this.destination = destination;
		this.destTime = destTime;
		this.price = price;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( (destTime == null) ? 0 : destTime.hashCode());
		result = prime * result + ( (destination == null) ? 0 : destination.hashCode());
		result = prime * result + ( (orginTime == null) ? 0 : orginTime.hashCode());
		result = prime * result + ( (origin == null) ? 0 : origin.hashCode());
		result = prime * result + ( (price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		FlightRecord other = (FlightRecord)obj;
		if( destTime == null )
		{
			if( other.destTime != null )
				return false;
		}
		else if( !destTime.equals( other.destTime ) )
			return false;
		if( destination == null )
		{
			if( other.destination != null )
				return false;
		}
		else if( !destination.equals( other.destination ) )
			return false;
		if( orginTime == null )
		{
			if( other.orginTime != null )
				return false;
		}
		else if( !orginTime.equals( other.orginTime ) )
			return false;
		if( origin == null )
		{
			if( other.origin != null )
				return false;
		}
		else if( !origin.equals( other.origin ) )
			return false;
		if( price == null )
		{
			if( other.price != null )
				return false;
		}
		else if( !price.equals( other.price ) )
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "FlightRecord [origin=" + origin + ", orginTime=" + orginTime + ", destination=" + destination
				+ ", destTime=" + destTime + ", price=" + price + "]";
	}

	@Override
	public int compareTo( FlightRecord other )
	{
		int priceCompare = this.price.compareTo( other.price );

		// if we are equal the sort on the next value
		if( priceCompare == 0 )
		{
			return this.orginTime.compareTo( other.orginTime );
		}
		else
		{
			return priceCompare;
		}

	}

	/**
	 * Get a textual representation of this record
	 * 
	 * @return
	 */
	public String getTextRepresentation()
	{
		DateTimeFormatter fmt = DateTimeFormat.forPattern( "M/d/y HH:mm:ss" );

		return String.format( OUTPUT_TEMPLATE, this.origin,
				this.destination,
				fmt.print( orginTime ),
				fmt.print( destTime ),
				price );
	}

}
