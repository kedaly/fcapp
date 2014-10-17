package com.flightcenter.command;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.flightcenter.demo.PriceProvider;
import com.flightcenter.demo.DAO.FlightRecord;
import com.flightcenter.demo.impl.MultiProvider;
import com.flightcenter.demo.impl.Provider1;
import com.flightcenter.demo.impl.Provider2;
import com.flightcenter.demo.impl.Provider3;
import com.google.common.collect.Sets;

public class Main
{

	public static void main( String[] args ) throws IOException
	{
		Set<PriceProvider> providers = Sets.newHashSet();
		providers.add( new Provider1() );
		providers.add( new Provider2() );
		providers.add( new Provider3() );

		try (MultiProvider lookupProvider = new MultiProvider( providers ))
		{
			;

			String org = null;
			String dest = null;

			// let's parse out the -o flag
			boolean oDetected = false;
			boolean dDetected = false;

			for( String arg : args )
			{
				// if we detected -o then the next is orgin
				if( oDetected )
				{
					org = arg;
					oDetected = false;
				}

				// dest
				if( dDetected )
				{
					dest = arg;
					dDetected = false;
				}

				// -o detection
				if( arg.equals( "-o" ) )
				{
					oDetected = true;
				}

				// -d detection
				if( arg.equals( "-d" ) )
				{
					dDetected = true;
				}

			}
			if( (dest != null) && (org != null) )
			{
				List<FlightRecord> records = lookupProvider.searchByRoute( org, dest );

				for( FlightRecord record : records )
				{
					System.out.println( record.getTextRepresentation() );
				}

			}
			else
			{
				System.out.println( "Invalid invocation format  -o <orgin> -d <destination>" );

				// so if we are called from a script say something went wrong
				System.exit( 1 );
			}
		}
		catch( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit( 1 );
		}
	}

}
