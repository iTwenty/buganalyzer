package org.j2t.buganalyzer;

import java.io.IOException;
import java.io.InputStream;

public class SampleProject
{
    String projectPath;
    private String projectError;
    private String projectOutput;
    Bug[]  projectBugs;
    
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

    public String getProjectError( )
    {
        return projectError;
    }

    public String getProjectOutput( )
    {
        return projectOutput;
    }
}