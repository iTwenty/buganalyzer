package org.j2t.buganalyzer.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.j2t.buganalyzer.app.SampleProject;

public class MyDialog extends JDialog
{
    String projectPath;
    
    JPanel mainPanel = new JPanel( );
    JPanel selectFilePanel = new JPanel( );
    JPanel resultPanel = new JPanel( );
    JPanel outputPanel = new JPanel( );
    JPanel errorsPanel = new JPanel( );
    
    JButton execJarButton;
    
    JScrollPane outputPane;
    JTextArea outputText;
    JLabel outputLabel;
    
    JScrollPane errorsPane;
    JTextArea errorsText;
    JLabel errorsLabel;

    public MyDialog( )
    {
        this.setSize( 600, 600 );
        this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        initializePanels( );
        initializeSelectFile( );
        initializeResults( );
        this.add( mainPanel );
        this.setVisible( true );
    }
    
    public void initializePanels( )
    {
        mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.Y_AXIS ) );
        selectFilePanel.setLayout( new FlowLayout( ) );
        resultPanel.setLayout( new FlowLayout( ) );
        outputPanel.setLayout( new BoxLayout( outputPanel, BoxLayout.Y_AXIS ) );
        errorsPanel.setLayout( new BoxLayout( errorsPanel, BoxLayout.Y_AXIS ) );
    }
    
    public void initializeSelectFile( )
    {
        execJarButton = new JButton( "Execute JAR" );
        selectFilePanel.add( execJarButton );
        execJarButton.addActionListener( new ActionListener( )
        {
            @Override
            public void actionPerformed( ActionEvent ev )
            {
                selectFile( );
            }
        } );
        mainPanel.add( selectFilePanel );
    }
    
    public void initializeResults( )
    {
        initializeOutput( );
        initializeErrors( );
    }
    
    public void initializeOutput( )
    {
        outputLabel = new JLabel( "Output" );
        outputText = new JTextArea( 10, 10 );
        outputPane = new JScrollPane( outputText );
        outputPanel.add( outputLabel );
        outputPanel.add( outputPane );
        mainPanel.add( outputPanel );
    }
    
    public void initializeErrors( )
    {
        errorsLabel = new JLabel( "Errors" );
        errorsText = new JTextArea( 10, 10 );
        errorsPane = new JScrollPane( errorsText );
        errorsPanel.add( errorsLabel );
        errorsPanel.add( errorsPane );
        mainPanel.add( errorsPanel );
    }
    
    public void selectFile( )
    {
        JFileChooser jfc = new JFileChooser( );
        FileNameExtensionFilter fnef = new FileNameExtensionFilter( "JAR Files", "jar" );
        jfc.setFileFilter( fnef );
        int returnVal = jfc.showOpenDialog( null );
        if( returnVal == JFileChooser.APPROVE_OPTION )
        {
            SampleProject sp = new SampleProject( jfc.getSelectedFile( ).getAbsolutePath( ) );
            try
            {
                sp.runProject( );
            }
            catch( IOException | InterruptedException e )
            {
                e.printStackTrace();
            }
            outputText.setText( sp.getProjectOutput( ) );
            errorsText.setText( sp.displayBugs( ) );
        }
    }
}
