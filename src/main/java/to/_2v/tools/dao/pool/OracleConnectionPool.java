package to._2v.tools.dao.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;


public class OracleConnectionPool extends ConnectionPool{
	private static final Log logger = LogFactory.getLog(OracleConnectionPool.class);
//	private static OracleConnectionPool instance = null;
	private DataSource ds;
	public OracleConnectionPool(MetadataInfo dbinfo){
		this.dbinfo = dbinfo;
	}
	
//	public static  OracleConnectionPool getInstance(MetadataInfo dbinfo) {
//		if (instance == null) {
//			instance = new OracleConnectionPool(dbinfo);
//		}
//		return instance;
//	}
	
	private void setDataSource(DataSource ds){
		this.ds = ds;
	}
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return conn;
	}
	
	public void setDataSource(){
		try {
			OracleDataSource ds = new OracleConnectionPoolDataSource ();
			
			ds.setURL(getUrl());
//			ds.setDriverType("thin");
//			ds.setServerName(Persist.HOST);
//			ds.setPortNumber(Integer.parseInt(Persist.ORACLE_PORT));
			ds.setDatabaseName(getDriverClassName());
			ds.setUser(dbinfo.getUser());
			ds.setPassword(dbinfo.getPassword());
			
			setDataSource(ds);
			if(checked && logger.isInfoEnabled())
				logger.info("DataSource: " + ds);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} 
		
	}
}
