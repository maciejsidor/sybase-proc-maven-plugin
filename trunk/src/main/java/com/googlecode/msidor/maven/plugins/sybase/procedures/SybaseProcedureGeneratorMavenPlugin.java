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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.Reader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sybase.jdbcx.SybDriver;

/**
 * @author Maciej SIDOR
 * 
 * Implementation of "generate-sql" goal.
 *   
 * <p>The following task are performed:
 * <ol>
 * <li> Parsing of stored procedure configuration file (if one has been given)</li>
 * <li> Establishing SYBASE connection</li>
 * <li> Retrieving stored procedures and all sub-procedures </li>
 * <li> Writing summary report HTML table with all procedures that have been identified and dependencies among them (optional)</li>
 * <li> Extracting SQL scripts for all stored procedures and all sub-procedures found</li>
 * <li> Deleting files that were not concerned by the generation (optional)</li>
 * </ol></p>
 * 
 * <p>Detecting sub-procedure calls is based on procedure body parsing (lookup for "exec" keyword) where commented calls are ignored.</p>
 * 
 * <p>The SQL scripts are written-out only if the SQL file for procedure doesn't exist yet or if the procedure body has been changed against the existing SQL file.</p>
 * 
 * <p>Additionally, this goal may generate HTML table row summarizing the procedure and its sub-procedures basic info. 
 * The table row is composed of 4 rows:
 * <ol>
 * <li>Nested level (0 for top level procedure)</li>
 * <li>Procedure name</li>
 * <li>Database name</li>
 * <li>Compilation date</li>
 * </ol>
 * This report file might be used after to publish the procedure configuration in CONFLENCE by calling the "publish" goal. 
 * </p>
 * 
 * @goal generate-sql
 */
public class SybaseProcedureGeneratorMavenPlugin extends AbstractMojo
{

    /**
     * The SYBASE user. REQUIRED.
     * @parameter expression="${sqlscript.sybase.user}"
     */
    private String syabseUser                       = null;

    /**
     * The SYBASE password. REQUIRED.
     * @parameter expression="${sqlscript.sybase.password}"
     */    
    private String sybasePassword                   = null;

    /**
     * The SYBASE connection string. REQUIRED.
     * @parameter 
     */    
    private String connectionString                 = null;

    /**
     * The list of procedures to check.
     * The procedure name should be composed of database name followed by ".." and the procedure name
     * @parameter 
     */        
    private List<String> proceduresToCheck          = null;

    /**
     * <br>The procedures configuration file.
     * The file should be a valid XML file.</br>
     * 
     * <br>There is no strict file structure required. 
     * The only prerequisite concerns the configuration of the stored procedure which should be encapsulated between &lt;storedProcedureConfiguration&gt; tag and should contain &lt;name&gt; and &lt;baseName&gt; tags indicating procedure name and database name respectively.</br>
     * 
     * <br>If this parameter is configured, the proceduresToCheck is ignored.</br>
     * 
     * @parameter 
     */        
    private String proceduresConfigFile             = null;

    /**
     * The path to put generated sources. REQUIRED
     * @parameter 
     */        
    private String outputDir                        = null;
    
    /**
     * Write summary report HTML table to that file under "outputDir" directory
     * @parameter 
     */        
    private String htmlOutputFile                   = "report.html";    
    
    /**
     * If true, all SQL files that are in output directory and haven't were not identified will be deleted.
     * @parameter 
     */        
    private boolean deleteUnusedSQLFiles            = true;

    /**
     * The implementation of "generate-sql" goal method.
     * 
     * The following task are performed:
     * <ol>
     * <li> Parsing of stored procedure configuration file (if one has been given)</li>
     * <li> Establishing SYBASE connection</li>
     * <li> Retrieving stored procedures and all sub-procedures </li>
     * <li> Writing summary report HTML table with all procedures that have been identified and dependencies among them (optional)</li>
     * <li> Extracting SQL scripts for all stored procedures and all sub-procedures found</li>
     * <li> Deleting files that were not concerned by the generation (optional)</li>
     * </ol>
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        /********************************************************************
         * Initialization
         ********************************************************************/
        
        //normalize output directory path
        if ( outputDir != null && !outputDir.endsWith( File.separator ) )
        {
            outputDir += File.separator;
        }
               
        //checking if output directory exists
        File outputDirFile = new File(outputDir);        
        if(!outputDirFile.exists())
            throw new MojoExecutionException( "Output direcotry doesn't exist" );
            

        /********************************************************************
         * Reading Procedures Configuration File 
         ********************************************************************/
        if ( proceduresConfigFile != null && proceduresConfigFile.length() > 0 )
        {
            getLog().info( "Parsing procedures config file..." );
            
            //clear any procedures that have been set manually in plugin configuration
            if(proceduresToCheck!=null)
                proceduresToCheck.clear();
            else
            {
                proceduresToCheck= new ArrayList<String>();
            }

            try
            {
                //----------------
                //parsing XML file
                //----------------
                
                //prepare document object
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse( new File( proceduresConfigFile ) );
                doc.getDocumentElement().normalize();

                //get root element
                NodeList nList = doc.getElementsByTagName( "storedProcedureConfiguration" );

                //go thought all stored procedure configuration element found
                for ( int temp = 0; temp < nList.getLength(); temp++ )
                {
                    Node nNode = nList.item( temp );

                    //check if the element is the XML node 
                    if ( nNode.getNodeType() == Node.ELEMENT_NODE )
                    {
                        //get the base and the procedure name
                        Element eElement = (Element) nNode;
                        String procName = eElement.getElementsByTagName( "name" ).item( 0 ).getFirstChild().getNodeValue();
                        String baseName = eElement.getElementsByTagName( "baseName" ).item( 0 ).getFirstChild().getNodeValue();

                        //put the configuration back to the list respecting the way like it would be configured manually in plugin configuration*
                        //(due to simplify the further part of procedures checking)
                        proceduresToCheck.add( baseName + ".." + procName );

                    }
                }

            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Could not parse the procedures config file", e );
            }
        }

        /********************************************************************
         * Connecting to SYBASE
         ********************************************************************/        
        Connection connection = null;
        try
        {
            getLog().info( "Connecting to SYBASE..." );

            //prepare the driver
            SybDriver sybDriver = (SybDriver) Class.forName( "com.sybase.jdbc3.jdbc.SybDriver" ).newInstance();
            sybDriver.setVersion( com.sybase.jdbcx.SybDriver.VERSION_3 );
            DriverManager.registerDriver( sybDriver );

            //set the connection properties
            Properties prop = new Properties();
            prop.setProperty( "user", syabseUser );
            prop.setProperty( "password", sybasePassword );
            prop.setProperty( "USE_METADATA", "true" );
            prop.setProperty( "IGNORE_DONE_IN_PROC", "false" );
            
            //connect
            connection = java.sql.DriverManager.getConnection( connectionString, prop );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Could not establish a SYBASE connection", e );
        }

        /********************************************************************
         * Check the procedures
         ********************************************************************/      
        //this collection stores all procedures that have been configured and have been successfully retrieved from SYABSE
        ArrayList<Procedure> topLevelProcedures = new ArrayList<Procedure>();
        try
        {
            getLog().info( "Checking procedures..." );

            //go through all configured procedures
            for ( String procedureToCheck : proceduresToCheck )
            {
                //split procedure configuration by database and procedure name
                int index = procedureToCheck.indexOf( ".." );

                //if ".." was found and is neither the first nor the last character
                if ( index > 0 && procedureToCheck.length() > index + 2 )
                {
                    //get procedure and the 
                    String procName = procedureToCheck.substring( index + 2 );
                    String dbName = procedureToCheck.substring( 0, index );

                    //check the procedure
                    Procedure proc = checkProcedure( connection, procName, dbName );

                    //add to top level procedures if the procedure was found
                    if ( proc != null )
                        topLevelProcedures.add( proc );

                }
                else
                    throw new MojoExecutionException( "Procedure " + procedureToCheck + " configured incorrectly" );

            }

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error occurced while checking the procedure details", e );
        }
        finally
        {
            //try to gently disconnect from SYBASE
            
            try
            {
                if ( connection != null && !connection.isClosed() )
                    connection.close();
            }
            catch ( SQLException e )
            {
                throw new MojoExecutionException( "Error occurced while closing DB connection", e );
            }
        }
        
        
        
        /********************************************************************
         * HTML report table generation
         ********************************************************************/
        
        if(htmlOutputFile!=null && !htmlOutputFile.isEmpty())
        {
            File htmlFile = new File(outputDirFile,htmlOutputFile);
            
            try
            {
                getLog().info( "Generating HTML report table" );
    
                //construct the headers
                StringBuilder builder = new StringBuilder();
                builder.append("<table>");
                builder.append( "<tr>");   
                builder.append( "<th>Level</th>");   
                builder.append( "<th>Name</th>");   
                builder.append( "<th>Database</th>");   
                builder.append( "<th>Compiled</th>");   
                builder.append( "</tr>");   
                
                //this set keep all procedures names being reported 
                //so that a sub-procedure is reported only once per top  
                //level procedure thus avoiding endless looping 
                HashSet<String> proceduresReported = new HashSet<String>();
                
                //go through all top level procedures            
                for ( Procedure procedure : topLevelProcedures )
                {
                    //clear the list of reported procedures for each top level procedure
                    proceduresReported.clear();    
                    
                    //generate HTML table for top level procedure and all sub-procedures recursively
                    generateHtml( 0, procedure, proceduresReported, builder );
                }
                builder.append("</table>");
                
                //write the report file
                BufferedWriter writer = new BufferedWriter(new FileWriter( htmlFile ));
                writer.write( builder.toString() );
                writer.flush();
                writer.close();          
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Error occurced while updating CONFLUENCE page", e );
            }        
        }

        
        /********************************************************************
         * Output procedures sources
         ********************************************************************/
        
        //this set keeps names of all stored procedure printed/verified so one procedure is printed only once
        //if the SQL file for procedure exists already and hasn't been modified, the files is not overridden.
        //However... 1) the name of the procedure is put on the list 2) all sub-procedures are still being checked recursively
        HashSet<String> proceduresPrinted = new HashSet<String>();
        try
        {
            getLog().info( "Writing SQL scripts..." );

            //output all the SQL scripts for identified stored procedures
            for ( Procedure procedure : topLevelProcedures )
            { 
                //output top level procedure and its all sub procedures recursively unless the procedure has been printed already 
                writeScript( procedure, proceduresPrinted );
            }

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error occurced while writing SQL scripts", e );
        }
        
        
        /********************************************************************
         * Deleting files that were not concerned by the generation
         ********************************************************************/        
        if(deleteUnusedSQLFiles)
        {
            try
            {
                getLog().info( "Deleting unused files..." );
                
                //get all the SQL files that are currently in the output directory
                File[] listOfFilesInOutputDir = outputDirFile.listFiles( new FilenameFilter()
                {
                    
                    @Override
                    public boolean accept( File dir, String name )
                    {
                        if(name.toLowerCase().endsWith( ".sql" ))
                            return true;
                        else
                            return false;
                    }
                } );
    
                
                //go through all files found
                for ( File fileToCheck : listOfFilesInOutputDir )
                {
                    //check if file has been printed/verified during the generation 
                    if(!proceduresPrinted.contains( fileToCheck.getName().toLowerCase() ))
                    {
                        getLog().debug( "Deleting file "+fileToCheck.getName() );
                        
                        //delete the file if not in the list
                        fileToCheck.delete();
                    }                    
                }
    
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Error occurced while deleting unused SQL scripts", e );
            }        
        }


    }
    
    /**
     * Generate HTML table row summarizing the procedure and put it to the StringBuilder parameter. 
     * The table row is composed of 4 rows:
     * <ol>
     * <li>Nested level (0 for top level procedure)</li>
     * <li>Procedure name</li>
     * <li>Database name</li>
     * <li>Compilation date</li>
     * </ol>
     * 
     * The method is called recursively for all sub-procedures of the main procedure.
     * 
     * The "proceduresPrinted" collection contains names of all procedures being printed.
     * - if a procedure is already on the list, it is neither printed nor checked.
     * - if not, the HTML table row is printed, procedure name is added to the list and all its sub-procedures are checked  
     * 
     * @param level - the nested level
     * @param procedure - the current procedure to check
     * @param proceduresPrinted - The "proceduresPrinted" collection contains names of all procedures being printed.
     * @param sb - output string buffer
     * @throws Exception - if an error occurred during procedures printing 
     */
    private void generateHtml(int level, Procedure procedure, HashSet<String> proceduresPrinted, StringBuilder sb ) throws Exception
    {
       
        //print the procedure only if not yet on the list
        if ( !proceduresPrinted.contains( procedure.getName() ) )
        {
            //add procedure to "already printed"
            proceduresPrinted.add( procedure.getName());
            
            //print the row
            sb.append( "<tr>");   
            sb.append(  "<td>"+level+"</td>");                           
            sb.append( "<td>"+procedure.getName()+"</td>");   
            sb.append( "<td>"+procedure.getDbName()+"</td>");   
            sb.append( "<td>"+procedure.getCreationDate()+"</td>");   
            sb.append( "</tr>");   

            //check all sub procedures recursively
            for ( Procedure subProcedure : procedure.getProcedures() )
            {
                generateHtml(level+1, subProcedure, proceduresPrinted,sb );
            }

        }

    }
    

    /**
     * Write SQL script for procedure and all its sub-procedures (unless the procedure is on the proceduresPrinted list).
     * @param procedure - procedure to print
     * @param proceduresPrinted - list of procedures already printed. If a procedure to print is on the list nothing is printed
     * @throws Exception - if an error occurred during SQL script writing
     */
    private void writeScript( Procedure procedure, HashSet<String> proceduresPrinted ) throws Exception
    {

        //get the file to the output SQL script
        File file = new File(outputDir + procedure.getDbName() + ".." + procedure.getName() + ".sql");
               
        //check if file was already printed
        if ( !proceduresPrinted.contains( file.getName().toLowerCase() ) )
        {
            
            //add the file to the list in order to avoid re-printing of the same procedure
            proceduresPrinted.add( file.getName().toLowerCase() );
            
            //determine if file should be written
            boolean canWrite=true;
            
            //check if file exists and if procedure body has changed
            if(file.exists())
            {                                
                //get MD5 checksum of SYBASE procedure body
                StringReader reader = new StringReader( procedure.getBody() );
                String procSum = createChecksum( reader );
                
                //read file content and get MD5 checksum
                FileReader fileReader = new FileReader(  file  );
                String fileSum = createChecksum( fileReader );
                
                //check if contents are different
                if(procSum!=null && procSum.equals( fileSum ))
                    canWrite = false;
            }
            
            //if procedure can be written print it out to the SQL file
            if(canWrite)
            {
                getLog().info("Writing file "+file.getName());
                BufferedWriter writer = new BufferedWriter( new FileWriter( file) );
                writer.write( procedure.getBody() );
                writer.flush();
                writer.close();                
            }
            else
            {
                getLog().debug("Skipping file "+file.getName()+" as it exists already and no changes has been found.");
            }

            //even if procedure was not modified (thus not printed) check all sub-procedures recursively anyway. 
            for ( Procedure subProcedure : procedure.getProcedures() )
            {
                writeScript( subProcedure, proceduresPrinted );
            }

        }

    }

    /**
     * Retrieve procedure basic info and its body from SYBASE. 
     * Additionally detects all sub-procedure calls with "exec" keyword and parses them recursively.
     * Ignores commented calls.
     * 
     * @param connection - SQL connection object
     * @param procedureName - name of procedure to parse
     * @param dbName procedure - name of database to parse
     * @return Parsed procedure object
     * @throws SQLException if error occurred during procedure parsing unless error code 17461 informing that the procedure does not exist  
     */
    private Procedure checkProcedure( Connection connection, String procedureName, String dbName ) throws SQLException
    {
        Statement statement = null;
        ResultSet rs = null;
        try
        {
            
            Procedure procedure = null;
            
            //prepare and execute the procedure verification statement
            statement = connection.createStatement();
            statement.execute( "exec " + dbName + "..sp_help " + procedureName );
            
            //get the result
            statement.getMoreResults();
            rs = statement.getResultSet();
            if ( rs != null && rs.next() )
            {
                //get the procedure basic info from SYBASE
                String name = rs.getString( "Name" );
                String creationDate = rs.getString( "Create_date" );
                
                //close the result set as no longer needed
                rs.close();
                statement.close();
                
                getLog().debug(dbName + ".." + name + " compiled " + creationDate );
                
                //create the procedure object
                procedure = new Procedure( dbName, name, creationDate );

                //prepare and execute the commend to retrieve procedure body
                String query = "exec " + dbName + "..sp_helptext " + procedureName;
                Statement stmt = connection.createStatement();
                stmt.execute( query );

                //The procedure body is output in several lines.
                //Those lines must be stick to each other in one variable
                StringBuilder b = new StringBuilder();
                boolean firstResult = true;
                while ( stmt.getMoreResults() || stmt.getUpdateCount() != -1 )
                {
                    ResultSet rs2 = stmt.getResultSet();
                    if ( rs2 != null )
                    {
                        while ( rs2.next() && !firstResult )
                        {
                            String ligne = rs2.getString( 1 );
                            b.append( ligne );
                        }
                        rs2.close();

                        firstResult = false;
                    }
                }

                //close the statement as no longer needed
                stmt.close();
                
                //update procedure object with procedure body
                String body = b.toString();
                procedure.setBody( body );

                //check for sub-procedure calls
                Pattern MY_PATTERN = Pattern.compile( "[Ee][Xx][Ee][Cc]((\\s*@[^=]+=\\s*)|(\\s+))([^\\s]+)" );
                Matcher m = MY_PATTERN.matcher( body );
                while ( m.find() )
                {
                    String subProcCall = m.group( 4 );

                    //check if procedure call is not commented with line comments "--"
                    int start = m.start();
                    String codeBefore = body.substring( 0, start );
                    int lastLineComment = codeBefore.lastIndexOf( "--" );
                    int lastLineEnd = codeBefore.lastIndexOf( "\n" );

                    //check if procedure call is not in block comment
                    int lastCommentBegin = codeBefore.lastIndexOf( "/*" );
                    int lastCommentEnd = codeBefore.lastIndexOf( "*/" );

                    //check if any comments are applied to the procedure call
                    boolean toIgnore = false;
                    if ( lastLineEnd < lastLineComment || lastCommentEnd < lastCommentBegin )
                        toIgnore = true;

                    //if not commented
                    if ( !toIgnore )
                    {
                        //extract the procedure and database name from sub-procedure call
                        String subProcedureDaatabseName = null;
                        String subProcedureName = null;
                        int firstPointIndex = subProcCall.indexOf( '.' );
                        if ( firstPointIndex > 0 )
                        {
                            subProcedureDaatabseName = subProcCall.substring( 0, firstPointIndex );
                            subProcedureName = subProcCall.substring( subProcCall.lastIndexOf( '.' ) + 1 );
                        }
                        else
                        {
                            subProcedureDaatabseName = dbName;
                            subProcedureName = subProcCall;
                        }

                        //check if sub-procedure was parsed already
                        Procedure subProcedure = Procedure.getExistingProcedure( subProcedureDaatabseName, subProcedureName );

                        //if not, parse it recursively 
                        if ( subProcedure == null )
                            subProcedure = checkProcedure( connection, subProcedureName,subProcedureDaatabseName );
    
                        //if successfully parsed, or already existing, add it to the sub-procedures list of current procedure
                        if ( subProcedure != null )
                            procedure.getProcedures().add( subProcedure );
                    }
                    else
                        getLog().warn( "Commented sub procedure call found \"" +subProcCall + "\" in  "+dbName+".."+procedureName);
                }

            }

            return procedure;
        }
        catch ( SQLException sqlException )
        {
            //ignore SQL exception informing that the procedure does not exist
            if ( 17461 == sqlException.getErrorCode() )
                getLog().warn( dbName + ".." + procedureName + " is missing" );
            else
                throw sqlException;
        }
        finally
        {

        }

        return null;

    }

    /**
     * Calculate checksum for file
     * @param filename Path to file
     * @return String representation of binary checksum
     * @throws Exception occurred while computing checksum
     */
    public String createChecksum( Reader fis )
        throws Exception
    {
        byte[] buffer = new byte[1024];
        char[] bufferStr = new char[1024];
        MessageDigest complete = MessageDigest.getInstance( "MD5" );
        int numRead;
        buffer = stringToBytesUTFCustom(bufferStr );
        do
        {
            //numRead = fis.read(buffer);
            numRead = fis.read( bufferStr );
            
            if ( numRead > 0 )
            {
                complete.update( buffer, 0, numRead );
            }
        }
        while ( numRead != -1 );

        fis.close();

        String result = "";
        byte[] checkSumInBytes = complete.digest();
        for ( byte b : checkSumInBytes )
        {
            result += Integer.toString( ( b & 0xff ) + 0x100, 16 ).substring( 1 );
        }

        return result;
    }

    /**
     * Transform char array to bytes array
     * @param buffer
     * @return bytes array representation of buffer
     */
    public static byte[] stringToBytesUTFCustom( char[] buffer )
    {
        byte[] b = new byte[buffer.length << 1];
        for ( int i = 0; i < buffer.length; i++ )
        {
            int bpos = i << 1;
            b[bpos] = (byte) ( ( buffer[i] & 0xFF00 ) >> 8 );
            b[bpos + 1] = (byte) ( buffer[i] & 0x00FF );
        }
        return b;
    }



}
