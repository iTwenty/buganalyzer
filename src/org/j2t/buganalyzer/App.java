package org.j2t.buganalyzer;

import java.io.IOException;
import java.util.Scanner;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class App
{
    public static final String DB_PATH = "target/neo4j-db";
    
    public static GraphDatabaseService gds = new EmbeddedGraphDatabase( DB_PATH );
    
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
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

    public static void main( String[] args ) throws IOException, InterruptedException
    {
        String projectPath;
        registerShutdownHook( gds );
        Scanner sc = new Scanner( System.in );
        System.out.println( "Enter path to sample project" );
        projectPath = sc.nextLine( );
        sc.close( );
        System.out.println( "Project Path:\t" + projectPath );
        SampleProjectRunner spr = new SampleProjectRunner( projectPath );
        spr.runProject( );
        if( spr.getError( ) != null )
        {
            Bug b = new Bug( spr.getError( ) );
            System.out.println( b.getTitle( ) );
        }
        gds.shutdown( );
    }
}
