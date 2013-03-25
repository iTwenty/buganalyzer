package org.j2t.buganalyzer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class SampleProjectRunner
{
    String sampleProjectPath;
    String output;
    String error;
    
    public SampleProjectRunner( String s )
    {
        this.sampleProjectPath = s;
    }
    
    public static String convertStreamToString( InputStream is )
    {
        Scanner s = new Scanner( is ).useDelimiter( "\\A" );
        return s.hasNext( ) ? s.next( ) : "";
    }
    
    public int runProject( ) throws IOException, InterruptedException
    {
        Process proc = Runtime.getRuntime( ).exec( "java -jar " + this.sampleProjectPath );
        InputStream op = proc.getInputStream( );
        InputStream err = proc.getErrorStream( );
        output = convertStreamToString( op );
        error = convertStreamToString( err );
        return proc.waitFor( );
    }
    
    public String getOutput(  )
    {
        return output;
    }
    
    public String getError( )
    {
        return error;
    }
}
