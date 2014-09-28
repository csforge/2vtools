package to._2v.tools.dao.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.Persist;


public class ODBCConnectionPool extends ConnectionPool{

	private static final Log logger = LogFactory.getLog(ODBCConnectionPool.class);
	private DataSource ds;
	
	public ODBCConnectionPool(MetadataInfo dbinfo){
		super(dbinfo);
	}
	
	private void setDataSource(DataSource ds){
		this.ds = ds;
	}
	@SuppressWarnings("restriction")
	public void setDataSource(){
		try {
			sun.jdbc.odbc.ee.ConnectionPoolDataSource cpds = new sun.jdbc.odbc.ee.ConnectionPoolDataSource(
					"OdbcPool");
			cpds.setDatabaseName(dbinfo.getDataSource());
//			cpds.setUser(dbinfo.getUser());
//			cpds.setPassword(dbinfo.getPassword());
			try{
				if (Persist.isNotBlank("CharSet"))
					cpds.setCharSet(Persist.getProperty("CharSet")); // optional property
				cpds.setLoginTimeout(Persist.getIntProperty("LoginTimeout")); // optional property
				cpds.setMinPoolSize(Persist.getProperty("MinPool"));
				cpds.setInitialPoolSize(Persist.getProperty("InitialPool"));
				cpds.setMaxPoolSize(Persist.getProperty("MaxActive"));
				cpds.setMaxIdleTime(Persist.getProperty("MaxIdleTime"));
				cpds.setTimeoutFromPool(Persist.getProperty("TimeoutFromPool"));
				cpds.setMaintenanceInterval(Persist.getProperty("MaintenanceInterval"));
			}catch(Exception ex){
				logger.error(ex.getMessage());
			}

//			InitialContext ic = new InitialContext();
//			ic.bind("JdbcOdbcPool", cpds);
			/*
			 * sun.jdbc.odbc.ee.DataSource odbc_ds = new
			 * sun.jdbc.odbc.ee.DataSource(); odbc_ds.setUser(Config.USERNAME);
			 * odbc_ds.setPassword(Config.PASSWORD);
			 * odbc_ds.setDatabaseName(Config.DSN); //ds = odbc_ds; InitialContext
			 * ic = new InitialContext(); ic.bind("OdbcDB", odbc_ds);
			 */
			setDataSource(cpds);
			if(checked && logger.isInfoEnabled())
				logger.info("DataSource: " + ds + "; DSN: "+ cpds.getDatabaseName());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
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
	
}
