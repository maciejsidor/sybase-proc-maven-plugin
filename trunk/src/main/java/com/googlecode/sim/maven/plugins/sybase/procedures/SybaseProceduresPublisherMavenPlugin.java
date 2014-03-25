package com.googlecode.sim.maven.plugins.sybase.procedures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

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
public class SybaseProceduresPublisherMavenPlugin extends AbstractMojo
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
     * @parameter expression="${sqlscript.confluence.user}"
     */        
    private String confleunceUser                   = null;

    /**
     * The CONFLUENCE password. REQUIRED
     * @parameter expression="${sqlscript.confluence.password}"
     */            
    private String confluencePassword               = null;

    /**
     * The ID of CONFLUENCE page to update. REQUIRED
     * @parameter
     */         
    private Object confluencePageID                 = null;

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
    private String htmlOutputFile                   = null;        



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
            getLog().info( "Updating confluence page" );

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
    

    @SuppressWarnings("unchecked")
    /**
     * Update confluence page with given content and defined header under the given keyword (or at the top of page in none given)
     * @param contentToAdd - content to put to confluence page
     * @throws Exception if error occurred during the confluence page update
     */
    private void updateConfluencePage(String contentToAdd) throws Exception
    {
       //get the connection string
       XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
       config.setServerURL(new URL(confluenceServer +"/rpc/xmlrpc"));
       XmlRpcClient client = new XmlRpcClient();
       client.setConfig(config);      
       
       //authenticate with user and password
       Object result = client.execute("confluence2.login",new String[]{confleunceUser,confluencePassword} );
       
       //get the confluence page
       HashMap<Object,Object> page = (HashMap<Object,Object>)client.execute("confluence2.getPage",new Object[]{result,confluencePageID} );
       getLog().debug("Current CONFLUENCE page: "+ page);
       
       //try to find the keyword under which the content will be put 
       String content = (String)page.get("content");
       int i = content.indexOf(confluenceKeyWordForUpdate);
       i+=confluenceKeyWordForUpdate.length();           
       
       //if keyword was found
       if(i>=0)
       {
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
       page = (HashMap<Object,Object>)client.execute("confluence2.updatePage",new Object[]{result,page,pageUpdateOptions} );
    }



}
