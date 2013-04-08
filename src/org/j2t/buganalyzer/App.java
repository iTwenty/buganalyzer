package org.j2t.buganalyzer;

import java.io.IOException;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class App
{
    public static final String DB_PATH = "target/neo4j-db";
    public static GraphDatabaseService gds = new EmbeddedGraphDatabase( DB_PATH );
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        String projectPath;
        BugAnalyzerHelper.registerShutdownHook( gds );
        //Scanner sc = new Scanner( System.in );
        //System.out.println( "Enter path to sample project" );
        projectPath = "C:/HelloWorld.jar";
        //sc.close( );
        SampleProject sp = new SampleProject( projectPath );
        sp.runProject( );
        if( sp.getProjectError( ) != null )
        {
            sp.setProjectBugs( sp.createBugsFromError( sp.getProjectError( ) ) );
            sp.assignCategoriesToBugs( );
            int count = 1;
            for( Bug a : sp.getProjectBugs( ) )
            {
                System.out.println( "-----Bug " + count + "------" );
                System.out.println( a.getTitle( )
                        + " --> " + a.getUnderlyingNode( )
                        .getSingleRelationship( Relationships.BELONGS_TO, Direction.OUTGOING ).getType( )
                        + " --> " + a.getCategory( ) );
                a.getCategory( );
                count++;
            }
        }
        gds.shutdown( );
    }
}
