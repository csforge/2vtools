package to._2v.tools.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.RowList;


public class SingleBean {
	private static final Log logger = LogFactory.getLog(SingleBean.class);
	private String dsn;
	private String path;
	private String dbms;
	private MetadataInfo dbInfo;

	public SingleBean(){
		lazy();
		if(Persist.getProperty(Persist.ODBC).equals(Persist.DSN))
			this.dsn = Persist.getProperty(Persist.ODBC_PATH);
		else
			this.dbms = Persist.getProperty(Persist.ODBC);
			this.path = Persist.getProperty(Persist.ODBC_PATH);
	}
	/**
	 * 
	 * @param dsn
	 */
	public SingleBean(String dsn){
		this.dsn = dsn;
	}
	/**
	 * 
	 * @param dbms
	 * @param path
	 */
	public SingleBean(String dbms,String path){
		this.dbms = dbms;
		this.path = path;
	}
	public SingleBean(MetadataInfo dbInfo){
		this.dbInfo = dbInfo;
	}
	public Connection getConnection(){
		try {
			if(dbInfo!=null)
				return getConnectionWithDatabaseInfo();
			else if(dsn==null)
				return this.getConnectionByPath();
			else
				return this.getConnectionByDSN();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
	private Connection getConnectionByDSN() throws Exception {
		String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "jdbc:odbc:" + dsn;

		Class.forName(driverName);
		
		return DriverManager.getConnection(url, Persist.getProperty(Persist.User), 
				Persist.getProperty(Persist.Password));
	}
	private Connection getConnectionByPath() throws Exception {
		String driverName="sun.jdbc.odbc.JdbcOdbcDriver";
		String url = "";
		if (dbms.equals(Persist.ACCESS)) {
			url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
				+ path ;
		} else if (dbms.equals(Persist.EXCEL)) {
			url = "jdbc:odbc:DRIVER=Microsoft Excel Driver (*.xls);DBQ="
					+ path + ";readonly=false";
		} else if (dbms.equals(Persist.DBASE)) {
			url = "jdbc:odbc:DRIVER=Microsoft dBase Driver (*.dbf);DBQ="
				+ path + ";readonly=false";
		} else if (dbms.equals(Persist.VFP)) {
			url = "jdbc:odbc:DRIVER={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB="
					+ path + ";Exclusive=Yes";
		}
		
		Class.forName(driverName);
		
		return DriverManager.getConnection(url, Persist.getProperty(Persist.User), 
				Persist.getProperty(Persist.Password));
	}
	private Connection getConnectionWithDatabaseInfo() throws Exception {
		Class.forName(dbInfo.getDriverName());
		
		return DriverManager.getConnection(dbInfo.getJdbcUrl(), dbInfo.getUser(), 
				dbInfo.getPassword());
	}
	public List getTableNames() {
		List LL = new ArrayList();
		DatabaseMetaData dbmd;
		Connection conn = getConnection();
		ResultSet rs = null;
		try {
			dbmd = conn.getMetaData();
			rs = dbmd.getTables(null,null,null,new String[]{"TABLE"});//dbmd.getTables("", "", "", null);
			while (rs.next()) {
				String TABLE_NAME = rs.getString("TABLE_NAME");
				LL.add(TABLE_NAME.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(rs);
			close(conn);
		}
		return LL;
	}

	public List getColumnName(String table) {
		int column_type = -1;
		List LL = new RowList();
		String sqlstr = "select * from " + table;
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement(
//					ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE
					);
			rs = stmt.executeQuery(sqlstr);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				String column = "";
				if (column_type == -1) {
					column = rsmd.getColumnName(j);
				} else {
					if (column_type == rsmd.getColumnType(j)) {
						column = rsmd.getColumnName(j);
					} else {
						continue;
					}
				}
				LL.add(column);
			}
		} catch (SQLException e) {
//			e.printStackTrace();
			logger.error(table+","+e.getMessage());
		}finally{
			close(rs);
			close(stmt);
			close(conn);
		}
		return LL;
	}
	protected PreparedStatement getPreparedStatement(Connection conn,String sqlstr) {
		if(logger.isInfoEnabled())
			logger.info(sqlstr);
		PreparedStatement psmt = null;
		try {
			psmt = conn.prepareStatement(sqlstr
//					ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE
			);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return psmt;
	}
	protected void setPreparedStatementValues(PreparedStatement psmt,Object[] values) throws SQLException{
		if (logger.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer("{");
			if (values != null && values.length > 0) {
				for (int i = 1; i <= values.length; i++) {
					psmt.setObject(i, values[i-1]);
					sb.append(values[i-1]).append(",");
				}
				sb.append("}");
				logger.debug(sb.toString());
			}
		} else {
			if (values != null && values.length > 0) {
				for (int i = 1; i <= values.length; i++) {
					psmt.setObject(i, values[i-1]);
				}
			}
		}
	}
	
	public List exeSelect(String sqlstr, Object[] values) { //multi- rows and columns
		Connection conn = getConnection();
	  PreparedStatement psmt = getPreparedStatement(conn,sqlstr);
		List results = exeSelect(psmt,values);
		close(psmt);
		close(conn);
		
		return results;
	}

	protected List exeSelect(PreparedStatement psmt,Object[] values) { //multi- rows and columns
		ResultSet rs = null;
		List RecordsLL = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			RecordsLL = new RowList();
			List FieldsLL = null; //columns

//			rs.beforeFirst();
			while (rs.next()) { //rows --> records
				FieldsLL = new RowList();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) { //columns --> fields
					Object field = rs.getObject(i);
					FieldsLL.add(field);
				}
				RecordsLL.add(FieldsLL);
			}
		}catch (SQLException e) {
//			e.printStackTrace();
			logger.warn(e.getMessage());
			logger.warn(toString(values));
		}finally {
			close(rs);
		}
		return RecordsLL;
	}
	public List fillRowsData(String sqlstr, Object[] values) {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = fillRowsData(psmt, values);
		close(psmt);
		close(conn);
		return results;
	}
	protected List fillRowsData(PreparedStatement psmt,Object[] values) {
		List results = new RowList();
		ResultSet rs = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				Hashtable table = new Hashtable();
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					String column = rsmd.getColumnName(j);
					Object field = rs.getObject(column);
					if (field != null)
						table.put(column, field);
				}
				results.add(table);
			}
		} catch (SQLException e) {
			logger.warn(e.getMessage());
			logger.warn(toString(values));
		}finally{
			close(rs);
		}
		
		return results;
	}
	public int exeUpdate(String sqlstr, Object[] values) {
		Connection conn = getConnection();
	  PreparedStatement psmt = getPreparedStatement(conn,sqlstr);
		
		int c = exeUpdate(psmt,values);
		close(psmt);
		close(conn);
		return c;
	}
	protected int exeUpdate(PreparedStatement psmt,Object[] values) {
		try {
		  setPreparedStatementValues(psmt,values);
		  
			return psmt.executeUpdate();
		} catch (SQLException e) {
			logger.warn(e.getMessage());
			logger.warn(toString(values));
			return 0;
		}
	}
	protected String toString(Object[] values) {
		if(values==null || values.length<1)
			return "";
		int max = values.length - 1;
		StringBuffer buf = new StringBuffer("{");
		if (values != null && values.length > 0) {
			for (int i = 0; i <= max; i++) {
				buf.append(values[i]);
				if (i < max)
					buf.append(", ");
			}
			buf.append("}");
		}
		return buf.toString();
	}
	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error("can't close connection. "+e.getMessage());
		}
	}
	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			logger.error("can't close preparedStatement. "+e.getMessage());
		}
	}
	public static void close(PreparedStatement psmt) {
		try {
			if (psmt != null) {
				psmt.close();
			}
		} catch (SQLException e) {
			logger.error("can't close preparedStatement. "+e.getMessage());
		}
	}
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
	private static void lazy(){
		if(logger.isInfoEnabled())
			logger.info("lazy...");
		if(!Persist.isReady()){
			Persist.configure("persist.properties");
		}
	}
}
