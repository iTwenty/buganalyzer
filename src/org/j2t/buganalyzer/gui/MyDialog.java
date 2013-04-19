package org.j2t.buganalyzer.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.j2t.buganalyzer.app.SampleProject;

public class MyDialog extends JDialog
{
    private static final long serialVersionUID = 1L;

    String       projectPath;

    JPanel       mainPanel       = new JPanel( );
    JPanel       selectFilePanel = new JPanel( );
    JTabbedPane  resultsPane     = new JTabbedPane( );
    JPanel       outputPanel     = new JPanel( );
    JPanel       errorsPanel     = new JPanel( );

    JButton      execJarButton;
    JFileChooser jarFileChooser;
    JLabel       jarFilePath;


    JScrollPane  outputPane;
    JTextArea    outputText;

    JScrollPane  errorsPane;
    JTextArea    errorsText;

    public MyDialog( )
    {
        this.setSize( 1000, 400 );
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
        selectFilePanel.setLayout( new GridLayout( 2, 1 ) );
        outputPanel.setLayout( new BoxLayout( outputPanel, BoxLayout.Y_AXIS ) );
        errorsPanel.setLayout( new BoxLayout( errorsPanel, BoxLayout.Y_AXIS ) );
    }
    
    public void initializeSelectFile( )
    {
        JPanel execJarSizePanel = new JPanel( new FlowLayout( ) );
        execJarButton = new JButton( "Execute JAR" );
        execJarSizePanel.add( execJarButton );
        jarFilePath =  new JLabel( "", JLabel.CENTER );
        selectFilePanel.add( execJarSizePanel );
        selectFilePanel.add( jarFilePath );
        execJarButton.addActionListener( new ActionListener( )
        {
            @Override
            public void actionPerformed( ActionEvent ev )
            {
                selectFile( );
            }
        } );
        jarFileChooser = new JFileChooser( );
        FileNameExtensionFilter fnef = new FileNameExtensionFilter( "JAR Files", "jar" );
        jarFileChooser.setFileFilter( fnef );
        mainPanel.add( selectFilePanel );
    }
    
    public void initializeResults( )
    {
        initializeOutput( );
        initializeErrors( );
        resultsPane.addTab( "Output", outputPanel );
        resultsPane.addTab( "Errors", errorsPanel );
        mainPanel.add( resultsPane );
    }
    
    public void initializeOutput( )
    {
        outputText = new JTextArea( 10, 10 );
        outputPane = new JScrollPane( outputText );
        outputPanel.add( outputPane );
    }
    
    public void initializeErrors( )
    {
        errorsText = new JTextArea( 10, 10 );
        errorsPane = new JScrollPane( errorsText );
        errorsPanel.add( errorsPane );
    }
    
    public void selectFile( )
    {
        int returnVal = jarFileChooser.showOpenDialog( null );
        if( returnVal == JFileChooser.APPROVE_OPTION )
        {
           runFile( );
        }
    }
    
    public void runFile( )
    {
        this.projectPath = jarFileChooser.getSelectedFile( ).getAbsolutePath( );
        jarFilePath.setText( projectPath );
        SampleProject sp = new SampleProject( projectPath );
        try
        {
            sp.runProject( );
        }
        catch( IOException | InterruptedException e )
        {
            e.printStackTrace();
        }
        outputText.setText( sp.getProjectOutput( ) );
        errorsText.setText( sp.getBugRelations( ) );
    }
}
