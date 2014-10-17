package com.flightcenter.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightcenter.demo.DAO.FlightRecord;
import com.flightcenter.rest.PriceProviderProvider;

@RestController
public class LookupFlights
{

	@Autowired
	PriceProviderProvider provider;

	@RequestMapping( "/searchFlights/{orgin}/{dest}" )
	public String searchFlights( @PathVariable final String orgin, @PathVariable final String dest,
			@RequestParam final String format ) throws JsonProcessingException
	{

		// let's get the records
		List<FlightRecord> response = provider.getProvider().searchByRoute( orgin, dest );

		if( format.equals( "json" ) )
		{
			ObjectMapper mapper = new ObjectMapper();

			return mapper.writeValueAsString( response );
		}
		else if( format.equals( "text" ) )
		{
			StringBuilder builder = new StringBuilder();
			for( FlightRecord rec : response )
			{
				builder.append( rec.getTextRepresentation() + "<br>" );
			}
			return builder.toString();
		}
		else
		{

			// TODO: Should probably throw an exception

			return "Invalid format";

		}

	}
}
