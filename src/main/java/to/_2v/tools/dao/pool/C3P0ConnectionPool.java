package to._2v.tools.dao.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.Persist;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

public class C3P0ConnectionPool extends ConnectionPool {

	private static final Log logger = LogFactory.getLog(C3P0ConnectionPool.class);
	private DataSource ds;
	public C3P0ConnectionPool(MetadataInfo dbinfo){
		this.dbinfo = dbinfo;
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
	public void setDataSource() {
		setComboPooledDs();
	}
	private void setDataSource(DataSource ds){
		this.ds = ds;
	}
	private void setComboPooledDs(){
		try {
			ComboPooledDataSource cpds = new ComboPooledDataSource();
			cpds.setDriverClass(getDriverClassName());	 //loads the jdbc driver 
			cpds.setJdbcUrl(getUrl());
			cpds.setUser(dbinfo.getUser()); 
			cpds.setPassword(dbinfo.getPassword()); // the settings below are optional -- c3p0 can work with defaults 
			cpds.setMinPoolSize(Persist.getIntProperty("MinPool")); 
			cpds.setAcquireIncrement(1); 
			cpds.setMaxPoolSize(Persist.getIntProperty("MaxActive")); 
			// The DataSource cpds is now a fully configured and usable pooled DataSource ...

			setDataSource(cpds);
			if(checked && logger.isInfoEnabled())
				logger.info("DataSource: " + ds);
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage());
		}
	}
//	void factory(){
//		try {
////			DataSource ds_unpooled = DataSources.unpooledDataSource(getUrl(), Persist.USER, Persist.PASSWORD);
////			DataSource ds_pooled = DataSources.pooledDataSource( ds_unpooled ); 
////			// The DataSource ds_pooled is now a fully configured and usable pooled DataSource. 
////			// The DataSource is using a default pool configuration, and Postgres' JDBC driver 
////			// is presumed to have already been loaded via the jdbc.drivers system property or an 
////			// explicit call to Class.forName("org.postgresql.Driver") elsewhere. ... 
////			
////			setDataSource(ds_pooled);
//			
//			DataSource ds_unpooled = DataSources.unpooledDataSource(getUrl(), Persist.USER, Persist.PASSWORD);
//			PoolConfig pc = new PoolConfig(); 
//			pc.setMaxStatements(200); //turn on Statement pooling 
//			pc.setMaxPoolSize(Persist.MaxActive);
//			pc.setMinPoolSize(Persist.MinPool);
//			pc.setAcquireIncrement(1);
//			// pass our overriding PoolConfig to the DataSources.pooledDataSource() factory method. 
//			DataSource ds_pooled = DataSources.pooledDataSource( ds_unpooled, pc ); 
//			// The DataSource ds_pooled is now a fully configured and usable pooled DataSource, 
//			// with Statement caching enabled for a maximum of up to 200 statements. ... 
//
//			setDataSource(ds_pooled);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	static void cleanup(DataSource ds){ // make sure it's a c3p0 PooledDataSource 
		if ( ds instanceof PooledDataSource) {
			PooledDataSource pds = (PooledDataSource) ds; 
			try {
				pds.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			} 
		} else 
			System.err.println("Not a c3p0 PooledDataSource!"); 
	}
//	CLOB getoracleCLOB(){
//		CLOB oracleCLOB = null;
//		try {
//			C3P0ProxyConnection castCon = (C3P0ProxyConnection) ds.getConnection();
//			Method m = CLOB.class.getMethod("createTemporary", new Class[]{Connection.class, boolean.class, int.class});
//			Object[] args = new Object[] {C3P0ProxyConnection.RAW_CONNECTION, Boolean.valueOf( true ), new Integer( 10 )};
//			oracleCLOB = (CLOB) castCon.rawConnectionOperation(m, null, args); 
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		} 
//		return oracleCLOB;
//	}
//	void jndi(){
//		// fetch a JNDI-bound DataSource 
//		InitialContext ictx;
//		try {
//			ictx = new InitialContext();
//			DataSource ds = (DataSource) ictx.lookup( "java:comp/env/jdbc/myDataSource" );
//			// make sure it's a c3p0 PooledDataSource 
//			if ( ds instanceof PooledDataSource) { 
//				PooledDataSource pds = (PooledDataSource) ds;
//				System.err.println("num_connections: " + pds.getNumConnectionsDefaultUser());
//				System.err.println("num_busy_connections: " + pds.getNumBusyConnectionsDefaultUser());
//				System.err.println("num_idle_connections: " + pds.getNumIdleConnectionsDefaultUser()); 
//				System.err.println(); 
//			} else 
//				System.err.println("Not a c3p0 PooledDataSource!");
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		} 
//	}
}
