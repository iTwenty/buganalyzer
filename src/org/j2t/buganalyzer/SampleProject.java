package org.j2t.buganalyzer;

import java.io.IOException;
import java.io.InputStream;

public class SampleProject
{
            String projectPath;
    private String projectError;
    private String projectOutput;
            Bug[ ] projectBugs;
    
    public SampleProject( String s )
    {
        this.projectPath = s;
    }
    
    public int runProject( ) throws IOException, InterruptedException
    {
        Process proc = Runtime.getRuntime( ).exec( "java -jar " + this.projectPath );
        InputStream op = proc.getInputStream( );
        InputStream err = proc.getErrorStream( );
        projectOutput = BugAnalyzerHelper.convertStreamToString( op );
        projectError = BugAnalyzerHelper.convertStreamToString( err );
        return proc.waitFor( );
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
                errors[j].setTitle( "\t" + errorLines[i] );
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
    
    public String getProjectError( )
    {
        return projectError;
    }
    
    public String getProjectOutput( )
    {
        return projectOutput;
    }
    
    public Bug[ ] getProjectBugs( )
    {
        return projectBugs;
    }
    
    public void setProjectBugs( Bug[ ] projectBugs )
    {
        this.projectBugs = projectBugs;
    }
}