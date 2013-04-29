package org.j2t.buganalyzer.app;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.j2t.buganalyzer.gui.MyDialog;

public class App
{
    public static final String DB_PATH = "target/neo4j-db";
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
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
