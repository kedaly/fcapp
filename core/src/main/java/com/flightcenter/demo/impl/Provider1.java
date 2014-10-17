package com.flightcenter.demo.impl;

import java.io.IOException;

/**
 * This class loads a file directly from the resources for testing purposes
 * in a real system, the provider interface would get the data from the correct source
 * 
 * @author kedaly7@gmail.com
 *
 */
public class Provider1 extends FileProvider
{
	public static String URI = "Provider1.txt";
	public static String DELIMITER = ",";
	public static String NAME = "Provider 1";
	public static String TIMEFORMAT = "M/d/Y H:m:s";

	public Provider1() throws IOException
	{
		super( NAME, URI, DELIMITER, TIMEFORMAT );
	}

}
