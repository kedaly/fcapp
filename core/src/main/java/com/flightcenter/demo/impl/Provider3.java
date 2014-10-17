package com.flightcenter.demo.impl;

import java.io.IOException;

public class Provider3 extends FileProvider
{
	public static String URI = "Provider3.txt";
	public static String DELIMITER = "|";
	public static String NAME = "Provider 2";
	public static String TIMEFORMAT = "M/d/Y H:m:s";

	public Provider3() throws IOException
	{
		super( NAME, URI, DELIMITER, TIMEFORMAT );
	}

}
