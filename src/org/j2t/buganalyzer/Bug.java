package org.j2t.buganalyzer;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Bug
{
    public static final String TITLE = "title";
    
    Node underlyingNode;
    Transaction tx;
    String title;
    
    public Bug( String title )
    {
        tx = App.gds.beginTx( );
        underlyingNode = App.gds.createNode( );
        underlyingNode.setProperty( TITLE, title );
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
    
    public Node getUnderlyingNode( )
    {
        return underlyingNode;
    }

    public void setUnderlyingNode( Node underlyingNode )
    {
        this.underlyingNode = underlyingNode;
    }
}
