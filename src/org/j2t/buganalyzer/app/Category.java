package org.j2t.buganalyzer.app;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class Category
{
    public static final String NAME = "name";
    
    Node underlyingNode;
    Transaction tx;
    String name;
    public GraphDatabaseService gds;
    
    public Category( String name, GraphDatabaseService gds )
    {
        this.gds = gds;
        tx = gds.beginTx( );
        underlyingNode = gds.createNode( );
        underlyingNode.setProperty( NAME, name );
        tx.success( );
        tx.finish( );
    }
    
    public String getName( )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        String s = ( String )( underlyingNode.getProperty( NAME ) );
        tx.success( );
        tx.finish( );
        return s;
    }
    
    public void setName( String name )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        underlyingNode.setProperty( NAME, name );
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
