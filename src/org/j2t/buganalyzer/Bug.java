package org.j2t.buganalyzer;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Bug
{
    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String LOCATION = "location";
    
    Node underlyingNode;
    Transaction tx;
    String title;
    String details;
    String location;
    
    public Bug( String title, String details, String location )
    {
        tx = App.gds.beginTx( );
        underlyingNode = App.gds.createNode( );
        underlyingNode.setProperty( TITLE, title );
        underlyingNode.setProperty( DETAILS, details );
        underlyingNode.setProperty( LOCATION, location );
        tx.success( );
        tx.finish( );
    }
    
    public String getTitle( )
    {
        String s =  ( String )( underlyingNode.getProperty( TITLE ) );
        return s;
    }
    
    public void setTitle( String title )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        underlyingNode.setProperty( TITLE, title );
        tx.success( );
        tx.finish( );
    }
    
    public String getDetails( )
    {
        String s =  ( String )( underlyingNode.getProperty( DETAILS ) );
        return s;
    }

    public void setDetails( String details )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        underlyingNode.setProperty( DETAILS, details );
        tx.success( );
        tx.finish( );
    }

    public String getLocation( )
    {
        String s =  ( String )( underlyingNode.getProperty( LOCATION ) );
        return s;
    }

    public void setLocation( String location )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        underlyingNode.setProperty( LOCATION, location );
        tx.success( );
        tx.finish( );
    }

    public Node getUnderlyingNode( )
    {
        return underlyingNode;
    }

    public void setUnderlyingNode( Node underlyingNode )
    {
        this.underlyingNode = underlyingNode;
    }
}
