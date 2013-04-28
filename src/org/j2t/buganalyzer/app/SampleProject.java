package org.j2t.buganalyzer.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class SampleProject
{
    String                      projectPath;
    private String              projectError;
    private String              projectOutput;
    private Process             process;
    Bug[]                       projectBugs;
    public GraphDatabaseService gds;
    
    public SampleProject( String path )
    {
        this.projectPath = path;
        gds = new EmbeddedGraphDatabase( App.DB_PATH + path.substring( path.lastIndexOf( "/" ) + 1, path.indexOf( "." ) ) );
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
            errors[k] = new Bug( "", "", "" );
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