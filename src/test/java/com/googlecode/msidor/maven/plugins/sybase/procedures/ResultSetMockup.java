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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Maciej SIDOR
 * 
 * Mockup implementation of ResultSet for test purpose.
 * Only the methods that are used by Sybase MOJOs are implemented.  
 */
public class ResultSetMockup implements ResultSet
{
	List<String> 	columns 	= new ArrayList<String>();
	List<String> 	values 		= new ArrayList<String>();
	int 			rowCount 	= 0;
	
	/*-------------------------Helpers-------------------------------------------------------*/
	public void addValue(String columnName, String value)
	{
		columns.add(columnName);
		values.add(value);
	}
	
	/*-------------------------Mocked interface implementations------------------------------*/
	@Override
	public String getString(int columnIndex) throws SQLException
	{
		return values.get(columnIndex-1);
	}

	@Override
	public String getString(String columnLabel) throws SQLException
	{
		int columnIndex = columns.indexOf(columnLabel);		
		return values.get(columnIndex);
	}
	
	@Override
	public boolean next() throws SQLException
	{
		rowCount++;
		return rowCount==1;
	}	
	
	
	/*-------------------------Fake interface implementations--------------------------------*/
	/*Don't expect any comments here as bellow implementations do nothing*/
	

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public boolean absolute(int row) throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public void afterLast() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void beforeFirst() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void cancelRowUpdates() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void clearWarnings() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void close() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void deleteRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public int findColumn(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public boolean first() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public int getFetchDirection() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public int getHoldability() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public int getInt(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public int getInt(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public long getLong(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public long getLong(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public int getRow() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public short getShort(int columnIndex) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public short getShort(String columnLabel) throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public Statement getStatement() throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public int getType() throws SQLException
	{
		//nothing to do
		return 0;
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		//nothing to do
		return null;
	}

	@Override
	public void insertRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public boolean isAfterLast() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean isFirst() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean isLast() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean last() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public void moveToCurrentRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void moveToInsertRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public boolean previous() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public void refreshRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public boolean relative(int rows) throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException
	{
		//nothing to do
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void setFetchSize(int rows) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNull(int columnIndex) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateNull(String columnLabel) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateRow() throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException
	{
		//nothing to do

	}

	@Override
	public boolean wasNull() throws SQLException
	{
		//nothing to do
		return false;
	}

}
