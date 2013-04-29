package org.j2t.buganalyzer.app;

import org.neo4j.graphdb.GraphDatabaseService;

public class BugCategories
{
    public static Category arithmeticError;
    public static Category classDefinitionError;
    public static Category xmlError;
    public static Category arrayIndexError;
    public static Category nullPointerError;
    
    public static void createCategories( GraphDatabaseService gds )
    {
        arithmeticError = new Category( "Arithmetic Error", gds );
        classDefinitionError = new Category( "Class Definition Error", gds );
        xmlError = new Category( "XML Error", gds );
        arrayIndexError = new Category( "Array Index Error", gds );
        nullPointerError = new Category( "Null Pointer Error", gds );
    }
}