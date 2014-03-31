/*
Copyright 2014 Maciej SIDOR [maciejsidor@gmail.com]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.	
 */
package com.googlecode.msidor.maven.plugins.sybase.procedures;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Maciej SIDOR
 * 
 * Object that contains all basic informations about the SYBASE stored procedure. 
 */
public class Procedure
{
    /**
     * The static collection of all created objects.
     * Allows to retrieve existing procedure instead of creating the doubled one 
     */
    private static Map<String, Procedure> allProcedures = new Hashtable<String, Procedure>();

    /**
     * Database name
     */
    private String dbName;

    /**
     * Procedure name
     */
    private String name;

    /**
     * Compilation date
     */
    private String creationDate;

    /**
     * Procedure body
     */
    private String body;

    /**
     * List if sub procedures called by this procedure
     */
    private List<Procedure> procedures = new ArrayList<Procedure>();

    
    /**
     * The only constructor
     * @param dbName - procedure database name
     * @param name - procedure name
     * @param creationDate - compilation date
     */
    public Procedure( String dbName, String name, String creationDate )
    {
        super();
        this.dbName = dbName;
        this.name = name;
        this.creationDate = creationDate;

        Procedure.allProcedures.put( dbName + ":" + name, this );
    }

    /**
     * Retrieves procedure from the collection of already created procedures
     * @param dbName - procedure database name
     * @param name - procedure name
     * @return Procedure if already created or null if none found
     */
    public static Procedure getExistingProcedure( String dbName, String name )
    {
        if ( !Procedure.allProcedures.containsKey( dbName + ":" + name ) )
            return null;
        else
            return Procedure.allProcedures.get( dbName + ":" + name );
    }

    /**
     * @return the dbName
     */
    public String getDbName()
    {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName( String dbName )
    {
        this.dbName = dbName;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate()
    {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate( String creationDate )
    {
        this.creationDate = creationDate;
    }

    /**
     * @return the body
     */
    public String getBody()
    {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody( String body )
    {
        this.body = body;
    }

    /**
     * @return the procedures
     */
    public List<Procedure> getProcedures()
    {
        return procedures;
    }

    /**
     * @param procedures the procedures to set
     */
    public void setProcedures( List<Procedure> procedures )
    {
        this.procedures = procedures;
    }



}
