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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;

/**
 * @author Maciej SIDOR
 *
 * Interface of DAO for Sybase MOJOs
 */
public interface SybaseProceduresMojoDAOI
{

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
	public abstract void updateConfluencePage(Object cookie, HashMap<Object, Object> page, HashMap<Object, Object> pageUpdateOptions) throws XmlRpcException;

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
	public abstract HashMap<Object, Object> getConfluencePage(Object cookie, String confluencePageID) throws XmlRpcException;

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
	public abstract Object authenticateToConfluence(String confleunceUser, String confluencePassword) throws XmlRpcException;

	/**
	 * Initialize XML RPC Client
	 * 
	 * @param confluenceServer
	 *            Confluence address
	 * @throws MalformedURLException
	 */
	public abstract void initializeXmlRpcClient(String confluenceServer) throws MalformedURLException;

	/**
	 * Connect to Sybase databse
	 * @param syabseDriverVersion Sybase driver version
	 * @param syabseUser Sybase user 
	 * @param sybasePassword Sybase password
	 * @param connectionString Sybase connection string
	 * @throws Exception
	 */
	public abstract void connectToSybase(String syabseDriver, String syabseUser, String sybasePassword, String connectionString) throws Exception;

	/**
	 * Disconnect from Sybase
	 * @throws SQLException
	 */
	public abstract void dropSybaseConnection() throws SQLException;

	/**
	 * Execute Sybase query
	 * @param Sybase query
	 * @return
	 * @throws SQLException
	 */
	public abstract Statement executeSybaseQuery(String query) throws SQLException;

}