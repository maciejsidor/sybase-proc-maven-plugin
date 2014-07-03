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
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.sybase.jdbcx.SybDriver;

/**
 * @author Maciej SIDOR
 *
 * Default implementation of DAO for Sybase MOJOs
 */
public class DefaultSybaseProceduresMojoDAO implements SybaseProceduresMojoDAOI
{
	
	public DefaultSybaseProceduresMojoDAO()
	{
	
	}
	
	/**
	 * The RPC client for connecting to Confluence
	 */
	private XmlRpcClient client = null;	
	
	/**
	 * The Sybase connection
	 */
	private Connection connection = null;
	
	/**
	 * Update Confluence page with given content
	 * 
	 * @param cookie
	 *            Confluence session authentication object
	 * @param page
	 *            Confluence page content
	 * @param pageUpdateOptions
	 *            Update options
	 * @throws XmlRpcException
	 */
	@Override
	public void updateConfluencePage(Object cookie, HashMap<Object, Object> page, HashMap<Object, Object> pageUpdateOptions) throws XmlRpcException
	{
		client.execute("confluence2.updatePage", new Object[] { cookie, page, pageUpdateOptions });
	}

	/**
	 * Retrieves Confluence page content
	 * 
	 * @param cookie
	 *            Confluence session authentication object
	 * @param confluencePageID
	 *            CID of Confluence page to retrieve
	 * @return Confluence page content
	 * @throws XmlRpcException
	 */
	@Override
	public HashMap<Object, Object> getConfluencePage(Object cookie, String confluencePageID) throws XmlRpcException
	{
		@SuppressWarnings("unchecked")
		HashMap<Object, Object> page = (HashMap<Object, Object>) client.execute("confluence2.getPage", new Object[] { cookie, confluencePageID });
		return page;
	}

	/**
	 * Authenticates to Confluence
	 * 
	 * @param confleunceUser
	 *            Confluence user
	 * @param confluencePassword
	 *            Confluence password
	 * @return Confluence session authentication object
	 * @throws XmlRpcException
	 */
	@Override
	public Object authenticateToConfluence(String confleunceUser, String confluencePassword) throws XmlRpcException
	{
		// authenticate with user and password
		Object result = client.execute("confluence2.login", new String[] { confleunceUser, confluencePassword });
		return result;
	}

	/**
	 * Initialize XML RPC Client
	 * 
	 * @param confluenceServer
	 *            Confluence address
	 * @throws MalformedURLException
	 */
	@Override
	public void initializeXmlRpcClient(String confluenceServer) throws MalformedURLException
	{
		// get the connection string
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(confluenceServer + "/rpc/xmlrpc"));
		client = new XmlRpcClient();
		client.setConfig(config);
	}
	
	/**
	 * Connect to Sybase databse
	 * @param syabseDriverVersion Sybase driver version
	 * @param syabseUser Sybase user 
	 * @param sybasePassword Sybase password
	 * @param connectionString Sybase connection string
	 * @throws Exception
	 */	
	@Override
	public void connectToSybase(String syabseDriver,String syabseUser, String sybasePassword, String connectionString) throws Exception
	{
        //prepare the driver
        SybDriver sybDriver = (SybDriver) Class.forName( syabseDriver ).newInstance();
        //sybDriver.setVersion( com.sybase.jdbcx.SybDriver.VERSION_3 );
        //sybDriver.setVersion(syabseDriverVersion);
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
	
	/**
	 * Disconnect from Sybase
	 * @throws SQLException
	 */	
	@Override
	public void dropSybaseConnection() throws SQLException
	{
        if ( connection != null && !connection.isClosed() )
            connection.close();
	}
	
	/**
	 * Execute Sybase query
	 * @param Sybase query
	 * @return
	 * @throws SQLException
	 */	
	@Override
	public Statement executeSybaseQuery(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		statement.execute(query);	
		
		return statement;
	}
}
