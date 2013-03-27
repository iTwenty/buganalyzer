package org.j2t.buganalyzer;



public class BugAnalyzerHelper
{
    public static Bug[] createBugsFromError( String error )
    {
        String[] errorLines = error.split( "\r?\n" );
        Bug[] errors = new Bug[ errorLines.length ];
        for( int k = 0; k < errors.length; ++k )
        {
            errors[k] = new Bug( "", "", "" );
        }
        int i = 0;
        int j = 0;
        while( i < errorLines.length )
        {
            if( ! ( errorLines[i].startsWith( "\t" ) ) )
            {
                if( i != 0 )
                {
                    errors[j].setLocation( errorLines[i-1] );
                    j++;
                }
                errors[j].setTitle( errorLines[i] );
            }
            else
            {
                errors[j].setDetails( errors[j].getDetails( ) + errorLines[i] + "\n" );
            }
            i++;
        }
        return errors;
    }
}
