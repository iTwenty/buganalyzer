package org.j2t.buganalyzer.app;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import org.gephi.graph.api.Attributes;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.neo4j.plugin.impl.Neo4jImporterImpl;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.tooling.GlobalGraphOperations;
import org.openide.util.Lookup;

public class SampleProject
{
    String                      projectPath;
    String                      dbName;
    private String              projectError;
    private String              projectOutput;
    private Process             process;
    Bug[]                       projectBugs;
    public GraphDatabaseService gds;
    
    public SampleProject( String path )
    {
        this.projectPath = path;
        this.dbName = App.DB_PATH + "-" + path.substring( path.lastIndexOf( "/" ) + 1, path.indexOf( "." ) );
        gds = new EmbeddedGraphDatabase( this.dbName );
        BugAnalyzerHelper.registerShutdownHook( gds );
        Runtime.getRuntime( ).addShutdownHook( new Thread( new Runnable( )
        {
            @Override
            public void run( )
            {
                Transaction tx = gds.beginTx( );
                for( org.neo4j.graphdb.Relationship r : GlobalGraphOperations.at( gds ).getAllRelationships( ) )
                {
                    r.delete( );
                }
                for( org.neo4j.graphdb.Node n : GlobalGraphOperations.at( gds ).getAllNodes( ) )
                {
                    n.delete( );
                }
                tx.success( );
                tx.finish( );
            }
        } ) );
    }
    
    public int runProject( ) throws IOException, InterruptedException
    {
        int retval;
        String[] cmdarray = { "java", "-jar", this.projectPath };
        Process process = new ProcessBuilder( cmdarray ).start( );
        InputStream op = process.getInputStream( );
        InputStream err = process.getErrorStream( );
        retval = process.waitFor( );
        projectOutput = BugAnalyzerHelper.convertStreamToString( op );
        projectError = BugAnalyzerHelper.convertStreamToString( err );
        if( projectError != null )
        {
            BugCategories.createCategories( gds );
            this.projectBugs = createBugsFromError( projectError );
            assignCategoriesToBugs( );
        }
        return retval;
    }
    
    public void assignCategoriesToBugs( )
    {
        for( Bug b : projectBugs )
        {
            if( b.getTitle( ).contains( "Exception" ) );
            {
                b.setCategory( BugCategories.Arithmetic );
            }
        }
    }
    
    public Bug[] createBugsFromError( String error )
    {
        String[] errorLines = BugAnalyzerHelper.removeEmptyLines( error.split( "\r?\n" ) );
        // variable denoting the no. of bugs found by counting lines not  beginning with tab.
        int count = 0;
        for( String s : errorLines )
        {
            if( ! ( s.startsWith( "\t" ) ) )
            {
                count++;
            }
        }
        Bug[] errors = new Bug[count];
        for( int k = 0; k < errors.length; ++k )
        {
            errors[k] = new Bug( "", "", "", gds );
        }
        int i = 0;
        int j = 0;
        while( i < errorLines.length )
        {
            // If current line does not begin with tab, it is the title of the current bug
            if( ! ( errorLines[i].startsWith( "\t" ) ) )
            {
                errors[j].setTitle( errorLines[i] );
            }
            // If it does begin with tab...
            else
            {
                // ...and if it's the last line or if the next line does not begins with tab...
                if( i == errorLines.length - 1 ? true : ! ( errorLines[i+1].startsWith( "\t" ) ) )
                {
                    // ...Set it as location of current bug and move on to next one...
                    errors[j++].setLocation( errorLines[i] );
                    // ...If bugs created equals the length of errors array, exit the while loop
                    if( j == errors.length )
                    {
                        break;
                    }
                }
                // ...Or else (in case it's not last line or the next line begins with tab )...
                else
                {
                    // ...Set it as details of current bug.
                    errors[j].setDetails( errors[j].getDetails( ) + errorLines[i] + "\n" );
                }
            }
            i++;
        }
        return errors;
    }
    
    public String getBugRelations( )
    {
        String s = "";
        if( projectBugs.length != 0 )
        {
            this.projectBugs = createBugsFromError( getProjectError( ) );
            assignCategoriesToBugs( );
            int count = 1;
            for( Bug a : projectBugs )
            {
                s += "-----Bug " + count + "------\n";
                s += a.toString( );
                s += "Category\n";
                s += ( "\t" + a.getCategory( ) );
                count++;
            }
        }
        return s;
    }
    
    public void generatePDF( String pdfPath )
    {
        
        Neo4jImporter ni = new Neo4jImporterImpl( );
        ni.importDatabase( gds );
        ProjectController pc = Lookup.getDefault( ).lookup( ProjectController.class );
        Workspace ws = pc.getCurrentWorkspace( );
        GraphModel gm = Lookup.getDefault( ).lookup( GraphController.class ).getModel( ws );
        for( Node n : gm.getDirectedGraph( ).getNodes( ) )
        {
            Attributes attr = n.getAttributes( );
            if( attr.getValue( Category.NAME ) != null )
                n.getNodeData( ).setLabel( ( String ) attr.getValue( Category.NAME ) );
            else if( attr.getValue( Bug.TITLE ) != null )
                n.getNodeData( ).setLabel( ( String ) attr.getValue( Bug.TITLE ) );
        }
        for( Edge e : gm.getDirectedGraph( ).getEdges( ) )
        {
            e.getEdgeData( ).setLabel( "Belongs To" );
        }
        PreviewModel pm = Lookup.getDefault( ).lookup( PreviewController.class ).getModel( );
        pm.getProperties( ).putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
        pm.getProperties( ).putValue( PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE );
        pm.getProperties( ).putValue( PreviewProperty.EDGE_CURVED, Boolean.FALSE );
        pm.getProperties( ).putValue( PreviewProperty.EDGE_COLOR, new EdgeColor( Color.GREEN ) );
        pm.getProperties( ).putValue( PreviewProperty.EDGE_THICKNESS, new Float( 0.1f ) );
        pm.getProperties( ).putValue( PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor( Color.BLACK ) );
        pm.getProperties( ).putValue( PreviewProperty.NODE_LABEL_FONT, pm.getProperties( ).getFontValue( PreviewProperty.NODE_LABEL_FONT ).deriveFont( 20 ) );
        ExportController ec = Lookup.getDefault( ).lookup( ExportController.class );
        try
        {
            ec.exportFile( new File( pdfPath.contains( ".pdf" ) ? pdfPath : pdfPath + ".pdf" ) );
            JOptionPane.showMessageDialog( null, "PDF File successfully generated" );
        }
        catch( IOException e ) { }
    }
    
    public String getProjectError( )
    {
        return projectError;
    }
    
    public String getProjectOutput( )
    {
        return projectOutput;
    }

    public Process getProcess( )
    {
        return process;
    }

    public Bug[ ] getProjectBugs( )
    {
        return projectBugs;
    }
}