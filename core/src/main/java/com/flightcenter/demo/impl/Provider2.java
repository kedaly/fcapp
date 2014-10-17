package com.flightcenter.demo.impl;

import java.io.IOException;

public class Provider2 extends FileProvider
{
	public static String URI = "Provider2.txt";
	public static String DELIMITER = ",";
	public static String NAME = "Provider 2";
	public static String TIMEFORMAT = "M-d-Y H:m:s";

	public Provider2() throws IOException
	{
		super( NAME, URI, DELIMITER, TIMEFORMAT );
	}

}
