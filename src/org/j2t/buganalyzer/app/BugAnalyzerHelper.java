package org.j2t.buganalyzer.app;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import org.neo4j.graphdb.GraphDatabaseService;

public class BugAnalyzerHelper
{
    public static String quotation = "\"";
    
    public static String[] removeEmptyLines( String[] errorLines )
    {
        CopyOnWriteArrayList<String> err = new CopyOnWriteArrayList<String>( Arrays.asList( errorLines ) );
        for( String s : err )
        {
            if( s.equals( "" ) )
            {
                err.remove( s );
            }
        }
        return err.toArray( new String[err.size( )] );
    }
    
    @SuppressWarnings ("resource" )
    public static String convertStreamToString( InputStream is )
    {
        Scanner s = new Scanner( is ).useDelimiter( "\\A" );
        return s.hasNext( ) ? s.next( ) : "";
    }
    
    public static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        Runtime.getRuntime( ).addShutdownHook( new Thread( )
        {
            @Override
            public void run( )
            {
                graphDb.shutdown( );
            }
        } );
    }
}
