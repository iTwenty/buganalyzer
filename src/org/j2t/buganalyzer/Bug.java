package org.j2t.buganalyzer;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

public class Bug
{
    public static final String TITLE = "title";
    public static final String DETAILS = "details";
    public static final String LOCATION = "location";
    
    private Node underlyingNode;
    private Transaction tx;
    private String title;
    private String details;
    private String location;
    private TraversalDescription td = Traversal.description( )
            .depthFirst( )
            .relationships( Relationships.BELONGS_TO, Direction.OUTGOING )
            .evaluator( Evaluators.toDepth( 1 ) )
            .evaluator( Evaluators.excludeStartPosition( ) );
    
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
    
    public void setCategory( Category c )
    {
        tx = underlyingNode.getGraphDatabase( ).beginTx( );
        this.underlyingNode.createRelationshipTo( c.getUnderlyingNode( ), Relationships.BELONGS_TO );
        tx.success( );
        tx.finish( );
    }
    
    public String getCategory( )
    {
        String s = null;
        for( Node n : td.traverse( this.underlyingNode ).nodes( ) )
        {
            s = ( String ) n.getProperty( Category.NAME );
        }
        return s;
    }
}
