package com.flightcenter.rest;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.impl.MultiProvider;

@Service
public class PriceProviderImpl implements PriceProviderProvider
{
	final PriceProvider provider;

	public PriceProviderImpl() throws IOException
	{
		this.provider = MultiProvider.getDefault();
	}

	@Override
	public PriceProvider getProvider()
	{
		return this.provider;
	}

}
