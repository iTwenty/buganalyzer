package org.j2t.buganalyzer.app;

import org.neo4j.graphdb.GraphDatabaseService;

public class BugCategories
{
    public static Category Arithmetic;
    
    public static void createCategories( GraphDatabaseService gds )
    {
        Arithmetic = new Category( "Arithmetic Error", gds );
    }
}