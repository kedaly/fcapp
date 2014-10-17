package com.flightcenter.demo;

import java.util.List;

import com.flightcenter.demo.DAO.FlightRecord;

/**
 * Provides the price and flight information for a given provider
 * Iterable<FlightRecord> is implemented in the case that we need to execute a query against
 * this. Since we have an unknown number of records implementations are responsible for paging and providing
 * the appropriate method for iterating.
 * 
 * @author kedaly7@gmail.com
 *
 */
public interface PriceProvider extends Iterable<FlightRecord>, AutoCloseable
{
	/**
	 * The name of the given provider
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get the number of records in the set
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * Get records that match a certain critera
	 * 
	 * @param orgin
	 * @param destination
	 * @return
	 */
	List<FlightRecord> searchByRoute( String orgin, String destination );

}
