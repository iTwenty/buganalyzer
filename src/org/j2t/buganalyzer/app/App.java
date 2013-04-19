package org.j2t.buganalyzer.app;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.neo4j.plugin.impl.Neo4jImporterImpl;
import org.j2t.buganalyzer.gui.MyDialog;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class App
{
    public static final String DB_PATH = "target/neo4j-db";
    public static GraphDatabaseService gds = new EmbeddedGraphDatabase( DB_PATH );
    
    public static void gephi( )
    {
        Neo4jImporter importer = new Neo4jImporterImpl( );
        importer.importDatabase( gds );
//        ProjectController pc = org.openide.util.Lookup.getDefault( ).lookup( ProjectController.class );
//        Workspace ws = pc.getCurrentWorkspace( );
//        GraphModel gm = org.openide.util.Lookup.getDefault( ).lookup( GraphController.class ).getModel( ws );
//        ForceAtlas2 layout = new ForceAtlas2( new ForceAtlas2Builder( ) );
//        layout.setGraphModel( gm );
    }
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        BugAnalyzerHelper.registerShutdownHook( gds );
        SwingUtilities.invokeLater( new Runnable( )
        {
            @Override
            public void run( )
            {
                MyDialog mf = new MyDialog( );
            }
        } );
    }
}
