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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.HashMap;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.logging.Logger;

/**
 * @author Maciej SIDOR
 * 
 * Default success test case. This is black box kind of test which means that
 * the test case prepares input data, executes plugin and validates outputs.
 * There are no unit tests on particular methods. This test case is based on
 * full plugin configuration. Here are some details about the test
 * configuration:
 * <ul>
 * <li>There should be 10 procedure files created</li>
 * <li>This test copies 3 procedures into test directory: one should be deleted, one should stay untouched and one should be overwriten</li>
 * <li>Confluence page is updated</li>
 * </ul>
 * 
 */
public class SybaseProceduresMojosDefaultTestCase extends AbstractMojoTestCase
{

	/** {@inheritDoc} */
	protected void setUp() throws Exception
	{
		// required
		super.setUp();
	}

	/** {@inheritDoc} */
	protected void tearDown() throws Exception
	{
		// required
		super.tearDown();
	}

	/**
	 * All test executer
	 * @throws Exception if any
	 */
	public void testBasicExecution() throws Exception
	{
		generatorBasicTestExecution();
		publisherBasicTestExecution();
	}
	
	
	/**
	 * The implementation of the default test case for "generate-sql" goal
	 * 
	 * @throws Exception
	 *             if any
	 */
	private void generatorBasicTestExecution() throws Exception
	{
		Logger logger = getContainer().getLogger();		
		logger.info("Launching default test case");		
		logger.info("Setting test data");
				
		//load the plugin configuration
		File pom = getTestFile("src/test/resources/com/googlecode/msidor/maven/plugins/sybase/procedures/generator-plugin-conf.xml");
		assertNotNull("POM file with polugin test configuration could not be found",pom);
		assertTrue("POM file with polugin test configuration could not be found",pom.exists());

		//prepare the mockup DAO
		SybaseProceduresMojoDAOMockup dao = new SybaseProceduresMojoDAOMockup();
		dao.setLogger(logger);
		
		//clean the out directory
		File dir = new File("out");
		for(File file: dir.listFiles()) file.delete();
		
		//copy procedures files
		copyFile("src/test/resources/com/googlecode/msidor/maven/plugins/sybase/procedures/base1..proc0.sql","out/base1..proc0.sql");
		copyFile("src/test/resources/com/googlecode/msidor/maven/plugins/sybase/procedures/base1..proc3.sql","out/base1..proc3.sql");
		copyFile("src/test/resources/com/googlecode/msidor/maven/plugins/sybase/procedures/base1..proc7.sql","out/base1..proc7.sql");
		
		
		ResultSetMockup rs = null;
		
		//---------procedure 1------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc1");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc1", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc1", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc1 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  exec proc9 product_id,type\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc1", rs);
		
		//---------procedure 2------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc2");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc2", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc2", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc2 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc2", rs);	
		
		//---------procedure 3------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc3");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc3", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc3", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc3 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc3", rs);	
		
		//---------procedure 4------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc4");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc4", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc4", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc4 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  exec proc9 product_id,type\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc4", rs);		
		
		//---------procedure 5------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc5");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base2..sp_help proc5", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base2..sp_helptext proc5", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc5 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"						
						+"END\n"								
						);
		dao.addQueryResult("exec base2..sp_helptext proc5", rs);			
		
		//---------procedure 6------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc6");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base3..sp_help proc6", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base3..sp_helptext proc6", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc6 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base3..sp_helptext proc6", rs);
		
		//---------procedure 7------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc7");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc7", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc7", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc7 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  --the change is here\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc7", rs);		
		
		//---------procedure 8------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc8");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc8", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc8", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc8 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc8", rs);
		
		//---------procedure 9------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc9");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc9", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc9", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc9 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  exec proc10 product_id,type\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc9", rs);	
		
		//---------procedure 10------------//
		rs = new ResultSetMockup();
		rs.addValue("Name", "proc10");
		rs.addValue("Create_date", "2014/09/23 11:10:12");
		dao.addQueryResult("exec base1..sp_help proc10", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("", "empty result set");
		dao.addQueryResult("exec base1..sp_helptext proc10", rs);
		
		rs = new ResultSetMockup();
		rs.addValue("",  "CREATE PROCEDURE proc10 (IN product_id INT, OUT type CHAR(10))\n"
						+"BEGIN\n"
						+"  DECLARE prod_name CHAR(20) ;\n"
						+"  SELECT name INTO prod_name FROM \"GROUPO\".\"Products\"\n"
						+"END\n"								
						);
		dao.addQueryResult("exec base1..sp_helptext proc10", rs);		

		//setup the mojo
		logger.info("Preparing plugin configuration");
		SybaseProceduresGeneratorMojo myMojo = (SybaseProceduresGeneratorMojo) lookupMojo("generate-sql", pom);
		myMojo.setDao(dao);
		assertNotNull("Plugin not found",myMojo);
				
		//execute the goal
		logger.info("Executing goal");
		logger.info("-------------------");
		myMojo.execute();
		logger.info("-------------------");
		logger.info("Validating results");
		
		//following files should be present
		File 	fileToCheck = null;
		String 	checksum	= null;
		
		fileToCheck = new File("out/base1..proc0.sql");
		assertTrue("base1..proc0.sql was not deleted",!fileToCheck.exists());
		
		fileToCheck = new File("out/base1..proc1.sql");
		assertTrue("base1..proc1.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc1.sql is incorrect. The new checksum is: "+checksum,"112ea029607cdbba0c38e7d9eb2206aa".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base1..proc2.sql");
		assertTrue("base1..proc2.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc2.sql is incorrect. The new checksum is: "+checksum,"4ec7bdb5bae5f2bba9fbde62771e9ef3".equalsIgnoreCase(checksum));

		fileToCheck = new File("out/base1..proc3.sql");
		assertTrue("base1..proc3.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc3.sql is incorrect. The new checksum is: "+checksum,"3c98870a4a3c376145904db911730781".equalsIgnoreCase(checksum));

		fileToCheck = new File("out/base1..proc4.sql");
		assertTrue("base1..proc4.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc4.sql is incorrect. The new checksum is: "+checksum,"7bc432a9d747c644110350423763bbbf".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base2..proc5.sql");
		assertTrue("base2..proc5.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base2..proc5.sql is incorrect. The new checksum is: "+checksum,"8bf084984a645f3facc22953c032a7b5".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base3..proc6.sql");
		assertTrue("base3..proc6.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base3..proc6.sql is incorrect. The new checksum is: "+checksum,"090d2f045c0686e91aab6e44e5060c13".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base1..proc7.sql");
		assertTrue("base1..proc7.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc7.sql is incorrect. The new checksum is: "+checksum,"4a9619de5e89b02ca6ce7455cb230d17".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base1..proc8.sql");
		assertTrue("base1..proc8.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc8.sql is incorrect. The new checksum is: "+checksum,"eb962544602e48b62ea33d567b85fad0".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base1..proc9.sql");
		assertTrue("base1..proc9.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc9.sql is incorrect. The new checksum is: "+checksum,"94f9bcd67b8bf153d2167cb8445ed346".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/base1..proc10.sql");
		assertTrue("base1..proc10.sql was not created",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of base1..proc10.sql is incorrect. The new checksum is: "+checksum,"0230a420b625653eb333d6039560d3a4".equalsIgnoreCase(checksum));
		
		fileToCheck = new File("out/report.html");
		assertTrue("report.html",fileToCheck.exists());
		checksum = createChecksum(fileToCheck);
		assertTrue("Checksum of report.html is incorrect. The new checksum is: "+checksum,"8696b238e7955cbde890bf57ca9132fc".equalsIgnoreCase(checksum));
		
		
		logger.info("Test accomplished succesfully");
		
	}
	
	/**
	 * The implementation of the default test case for "publish" goal
	 * 
	 * @throws Exception
	 *             if any
	 */
	private void publisherBasicTestExecution() throws Exception
	{
		Logger logger = getContainer().getLogger();		
		logger.info("Launching default test case");		
		logger.info("Setting test data");
				
		//load the plugin configuration
		File pom = getTestFile("src/test/resources/com/googlecode/msidor/maven/plugins/sybase/procedures/publisher-plugin-conf.xml");
		assertNotNull("POM file with polugin test configuration could not be found",pom);
		assertTrue("POM file with polugin test configuration could not be found",pom.exists());

		//prepare the mockup DAO
		SybaseProceduresMojoDAOMockup dao = new SybaseProceduresMojoDAOMockup();
		dao.setLogger(logger);	

		// add the confluence page
		HashMap<Object, Object> page = new HashMap<Object, Object>();
		page.put("space", "Doc");
		page.put("url", "http://confluence.organization.net/display/Doc/Project1+Release+Notes");
		page.put("version", Integer.valueOf(5));
		page.put("creator", "msidor");
		page.put("modified", "Wed Apr 02 10:45:35 CEST 2014");
		page.put("id", Integer.valueOf(25200500));
		page.put("content", "<p>begining of the page</p><p>end of the page</p>");
		page.put("modifier", "msidor");
		page.put("parentId", Integer.valueOf(25200485));
		page.put("title", "Project1 Release Notes");
		page.put("created", "Mon Mar 31 17:32:46 CEST 2014");
		page.put("contentStatus", "current");
		page.put("permissions", Integer.valueOf(0));
		page.put("current", Boolean.valueOf(true));
		page.put("homePage", Boolean.valueOf(false));
		dao.addConfluencePage("479a88d7bb24391678", page);
		
		
		//setup the mojo
		logger.info("Preparing plugin configuration");
		SybaseProceduresPublisherMojo myMojo = (SybaseProceduresPublisherMojo) lookupMojo("publish", pom);
		myMojo.setDao(dao);
		assertNotNull("Plugin not found",myMojo);
				
		//execute the goal
		logger.info("Executing goal");
		logger.info("-------------------");
		myMojo.execute();
		logger.info("-------------------");
		logger.info("Validating results");
		
		//check the confluence page
		HashMap<Object, Object> updatedPage = dao.getUpdatedConfluencePage();
		assertNotNull("Confluence page has not been updated",updatedPage);
																		
		assertTrue("Updated Confluence page is different than expected. New checksum : "+updatedPage.hashCode(),1836229332==updatedPage.hashCode());

		
		logger.info("Test accomplished succesfully");
		
	}	

	/**
	 * Calculate checksum for file
	 * 
	 * @param filename
	 *            Path to file
	 * @return String representation of binary checksum
	 * @throws Exception
	 *             occurred while computing checksum
	 */
	public String createChecksum(File file) throws Exception
	{
		FileReader fis = new FileReader(file);
		
		byte[] buffer = new byte[1024];
		char[] bufferStr = new char[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do
		{
			numRead = fis.read(bufferStr);

			// eliminate the date in checksum calculation. The example of string
			// to eliminate "date="Tue Jun 10 02:24:16 CEST 2014">"
			bufferStr = String.valueOf(bufferStr).replaceFirst("(<release version=\"[^\"]+\" )date=\"[^\"]+\">", "$1>").toCharArray();

			buffer = stringToBytesUTFCustom(bufferStr);

			if (numRead > 0)
			{
				complete.update(buffer, 0, numRead);
			}
		}
		while (numRead != -1);

		fis.close();

		String result = "";
		byte[] checkSumInBytes = complete.digest();
		for (byte b : checkSumInBytes)
		{
			result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
		}

		return result;
	}

	/**
	 * Transform char array to bytes array
	 * 
	 * @param buffer
	 * @return bytes array representation of buffer
	 */
	public static byte[] stringToBytesUTFCustom(char[] buffer)
	{
		byte[] b = new byte[buffer.length << 1];
		for (int i = 0; i < buffer.length; i++)
		{
			int bpos = i << 1;
			b[bpos] = (byte) ((buffer[i] & 0xFF00) >> 8);
			b[bpos + 1] = (byte) (buffer[i] & 0x00FF);
		}
		return b;
	}

	/**
	 * File copy
	 * @param sourceFileStr
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(String sourceFileStr, String destFileStr) throws IOException
	{
		File destFile = new File(destFileStr);
		
		if (!destFile.exists())
		{
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try
		{
			source = new FileInputStream(sourceFileStr).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally
		{
			if (source != null)
			{
				source.close();
			}
			if (destination != null)
			{
				destination.close();
			}
		}
	}
}
