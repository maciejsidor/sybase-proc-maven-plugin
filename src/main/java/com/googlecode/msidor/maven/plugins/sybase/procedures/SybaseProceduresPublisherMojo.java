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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @author Maciej SIDOR
 * 
 * Implementation of "publish" goal.
 * 
 * <p>Update confluence page with summary report HTML table with all procedures that have been identified and dependencies among them.</p>
 * 
 * <p>The HTML report table is taken from file identified by htmlOutputFile parameter. 
 * There should be only the HTML content to put to confluence (no HTML tag should be there).</p>
 * 
 * <p>The content is put under the under the given keyword (or at the top of page in none given nor found).</p> 
 * 
 * <p>Optionally, the update header might be specified.</p>
 *
 * @goal publish
 */
public class SybaseProceduresPublisherMojo extends AbstractMojo
{
    
    /**
     * The path to put generated sources. REQUIRED
     * @parameter 
     */        
    private String outputDir                        = null;    
    
    /**
     * The CONFLUENCE server address. REQUIRED
     * @parameter 
     */        
    private String confluenceServer                 = null; 

    /**
     * The CONFLUENCE user. REQUIRED
     * @parameter expression="${sybaseproc.confluence.user}"
     */        
    private String confleunceUser                   = null;

    /**
     * The CONFLUENCE password. REQUIRED
     * @parameter expression="${sybaseproc.confluence.password}"
     */            
    private String confluencePassword               = null;

    /**
     * The ID of CONFLUENCE page to update. REQUIRED
     * @parameter
     */         
    private String confluencePageID                 = null;

    /**
     * The keyword on CONFLUENCE page under which the new content will be put.
     * @parameter
     */             
    private String confluenceKeyWordForUpdate       = null;
    
    /**
     * The header for updated content.
     * @parameter
     */             
    private String updateHeader                     = null;
    
    /**
     * The Name of the summary report HTML table file under "outputDir" directory. REQUIRED
     * @parameter 
     */        
    private String htmlOutputFile                   = "report.html";        

    /**
     * The default DAO
     */
    private SybaseProceduresMojoDAOI dao 			= new DefaultSybaseProceduresMojoDAO();


    @Override
    /**
     * <p>Update confluence page with summary report HTML table with all procedures that have been identified and dependencies among them.</p>
     * 
     * <p>The HTML report table is taken from file identified by htmlOutputFile parameter. 
     * There should be only the HTML content to put to confluence (no HTML tag should be there).</p>
     * 
     * <p>The content is put under the under the given keyword (or at the top of page in none given nor found).</p> 
     * 
     * <p>Optionally, the update header might be specified.</p>
     */
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        
        //Ignore the execution as the plugin is not configured at all for the project
        if(outputDir==null)
            return;

        //normalize output directory path
        if ( outputDir != null && !outputDir.endsWith( File.separator ) )
        {
            outputDir += File.separator;
        }
               
        //check if the report HTML table file exists 
        File file = new File(outputDir + htmlOutputFile);        
        if(!file.exists())
            throw new MojoExecutionException( "Report file "+file.getName()+" was not found" );
            
        
        try
        {            
            getLog().info( "Updating confluence page..." );

            //read the HTML table from file
            BufferedReader reader = new BufferedReader(new FileReader( file ));
            StringBuilder builder = new StringBuilder(); 

            String line = null;
            while((line=reader.readLine())!=null)
            {
                builder.append( line );
            }            
            reader.close();
            
            //update confluence page
            updateConfluencePage(builder.toString());
            

        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Error occurced while updating CONFLUENCE page", e );
        }        



    }
    
    /**
     * Update confluence page with given content and defined header under the given keyword (or at the top of page in none given)
     * @param contentToAdd - content to put to confluence page
     * @throws Exception if error occurred during the confluence page update
     */
    private void updateConfluencePage(String contentToAdd) throws Exception
    {
       //get the connection string
       dao.initializeXmlRpcClient(confluenceServer);
       
       //authenticate with user and password
       Object result = dao.authenticateToConfluence(confleunceUser, confluencePassword);
       
       //get the confluence page
       HashMap<Object,Object> page = dao.getConfluencePage(result, confluencePageID);
       getLog().debug("Current CONFLUENCE page: "+ page);
       
       //try to find the keyword under which the content will be put 
       String content = (String)page.get("content");
       int i = -1;
       
       if(confluenceKeyWordForUpdate!=null)
           i = content.indexOf(confluenceKeyWordForUpdate);
       
       if(updateHeader==null)
           updateHeader="";
       
       //if keyword was found
       if(i>=0 )
       {
           i+=confluenceKeyWordForUpdate.length();
           content = content.substring(0, i)+updateHeader+contentToAdd+content.substring(i);
       }
       else
       {
           content = updateHeader+contentToAdd+content;                                      
       }
    
       
       getLog().debug("new content: "+content);
       
       //update confluence page
       page.put("content",content);       
       HashMap<Object,Object> pageUpdateOptions  = new HashMap<Object,Object>();
       dao.updateConfluencePage(result, page, pageUpdateOptions);
    }

	/**
	 * Set the DAO
	 * @param dao the dao to set
	 */
	public void setDao(SybaseProceduresMojoDAOI dao)
	{
		this.dao = dao;
	}



}
