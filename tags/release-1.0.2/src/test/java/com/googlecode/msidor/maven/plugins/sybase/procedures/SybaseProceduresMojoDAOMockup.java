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

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.plexus.logging.Logger;

/**
 * @author Maciej SIDOR
 * Mockup implementation of SybaseProceduresMojoDAOI. 
 */
public class SybaseProceduresMojoDAOMockup implements SybaseProceduresMojoDAOI
{

	private Hashtable<String, HashMap<Object, Object>> 	confluencePages 		= null;
	private HashMap<Object, Object> 					updatedConfluencePage	= null;	
	private String 										confleunceUser 			= null;
	private String 										confluencePassword 		= null;
	private String 										username 				= null;
	private String 										password 				= null;	
	private Logger										logger 					= null;
	
	private Hashtable<String, StatementMockup> 			results					= new Hashtable<String, StatementMockup>();

	
	/*-------------------------Interface implementation------------------------------*/
	@Override	
	/**
	 * Update Confluence page with given content.
	 * This implmentation sets updatedConfluencePage with given page.
	 * @param cookie Confluence session authentication object
	 * @param page Confluence page content
	 * @param pageUpdateOptions Update options
	 * @throws XmlRpcException
	 */	
	public void updateConfluencePage(Object cookie, HashMap<Object, Object> page, HashMap<Object, Object> pageUpdateOptions) throws XmlRpcException
	{
		if(logger!=null) 
			logger.info("SybaseProceduresMojoDAOMockup.updateConfluencePage: "+cookie+page+pageUpdateOptions);

		updatedConfluencePage = page;
	}

	@Override
	/**
	 * Retrieves Confluence page content.
	 * This iplmeentation returns confluence page that is set in confluencePages field under cookie+confluencePageID key.
	 * @param cookie Confluence session authentication object
	 * @param confluencePageID CID of Confluence page to retrieve
	 * @return Confluence page content
	 * @throws XmlRpcException
	 */	
	public HashMap<Object, Object> getConfluencePage(Object cookie, String confluencePageID) throws XmlRpcException
	{	
		if(logger!=null) 
			logger.info("SybaseProceduresMojoDAOMockup.getConfluencePage: "+cookie+confluencePageID);
		
		if(confluencePages!=null && confluencePages.containsKey(cookie+confluencePageID))
		{
			return confluencePages.get(cookie+confluencePageID);
		}		
		
		return null; 
	}

	@Override
	/**
	 * Authenticates to Confluence.
	 * This implementation sets confleunceUser and confluencePassword fields.
	 * @param confleunceUser Confluence user
	 * @param confluencePassword Confluence password
	 * @return Confluence session authentication object
	 * @throws XmlRpcException
	 */	
	public Object authenticateToConfluence(String confleunceUser, String confluencePassword) throws XmlRpcException
	{
		this.confleunceUser=confleunceUser;
		this.confluencePassword=confluencePassword;
		
		return "479a88d7bb";
	}

	@Override
	/**
	 * Initialize XML RPC Client.
	 * This implementation does nothing.
	 * @param confluenceServer Confluence address
	 * @throws MalformedURLException
	 */	
	public void initializeXmlRpcClient(String confluenceServer) throws MalformedURLException
	{
		// nothing to do here
	}

	@Override
	/**
	 * Connect to Sybase databse
	 * @param syabseDriverVersion Sybase driver version
	 * @param syabseUser Sybase user 
	 * @param sybasePassword Sybase password
	 * @param connectionString Sybase connection string
	 * @throws Exception
	 */	
	public void connectToSybase(String syabseDriverVersion, String syabseUser, String sybasePassword, String connectionString) throws Exception
	{
		// nothing to do here

	}

	@Override
	/**
	 * Disconnect from Sybase
	 * @throws SQLException
	 */	
	public void dropSybaseConnection() throws SQLException
	{
		// nothing to do here

	}

	@Override
	/**
	 * Execute Sybase query
	 * @param Sybase query
	 * @return
	 * @throws SQLException
	 */	
	public Statement executeSybaseQuery(String query) throws SQLException
	{
		logger.info("SybaseProceduresMojoDAOMockup: executing query \""+query+"\"");
		
		StatementMockup statement = null;
		
		if(results.containsKey(query))
			statement=results.get(query);
		
		return statement;
	}
	
	/*-------------------------Helpers------------------------------*/
	/**
	 * Add ResultSet that will be returned in Statement object for the given query.
	 * There may be one or more ResultSet registered for the same query. 
	 * @param query
	 * @param rs
	 */
	public void addQueryResult(String query,ResultSet rs)
	{
		StatementMockup statement = null;
		if(results.containsKey(query))
			statement=results.get(query);
		else
		{
			statement = new StatementMockup();
			results.put(query,statement);
		}
		
		statement.addResultSet(rs);
		
	}
	
	/**
	 * Add fake confluence page
	 * @param key
	 * @param content
	 */
	public void addConfluencePage(String key, HashMap<Object, Object> content)
	{
		if(confluencePages==null)
		{
			confluencePages = new Hashtable<String, HashMap<Object, Object>>();
		}
		
		confluencePages.put(key, content);
		
	}	
	
	/*-------------------------Getters and setters------------------------------*/
	
	public String getConfluencePassword()
	{
		return confluencePassword;
	}

	public void setConfluencePassword(String confluencePassword)
	{
		this.confluencePassword = confluencePassword;
	}

	public String getConfleunceUser()
	{
		return confleunceUser;
	}

	public void setConfleunceUser(String confleunceUser)
	{
		this.confleunceUser = confleunceUser;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}	
	
	public Logger getLogger()
	{
		return logger;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	public HashMap<Object, Object> getUpdatedConfluencePage()
	{
		return updatedConfluencePage;
	}

	public void setUpdatedConfluencePage(HashMap<Object, Object> updatedConfluencePage)
	{
		this.updatedConfluencePage = updatedConfluencePage;
	}
	
	

}
