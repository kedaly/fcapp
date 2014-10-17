package com.flightcenter.rest;

import com.flightcenter.demo.PriceProvider;

/**
 * Allows for the injection of the price provider
 * 
 * @author kevin
 *
 */
public interface PriceProviderProvider
{
	PriceProvider getProvider();
}
