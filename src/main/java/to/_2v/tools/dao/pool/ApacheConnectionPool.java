package to._2v.tools.dao.pool;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.Persist;


public class ApacheConnectionPool extends ConnectionPool {

	private static final Log logger = LogFactory.getLog(ApacheConnectionPool.class);
	
//	private static ApacheConnectionPool instance = null;
	private DataSourceConnectionFactory factory;
	private javax.sql.DataSource ds;
	public ApacheConnectionPool(MetadataInfo dbinfo){
		this.dbinfo = dbinfo;
	}
	
//	public static ApacheConnectionPool getInstance(MetadataInfo dbinfo) {
//		if (instance == null) {
//			instance = new ApacheConnectionPool(dbinfo);
//		}
//		return instance;
//	}
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = factory.createConnection();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (checked && logger.isDebugEnabled()) {
			logger.debug(conn + "@" + Integer.toHexString(hashCode()));
			StringBuffer sb = new StringBuffer();
			sb.append("NumActive: ").append(
					((BasicDataSource) ds).getNumActive()).append(", ").append(
					"NumIdle: ").append(((BasicDataSource) ds).getNumIdle());
			logger.debug(sb.toString());
		}
		return conn;
	}

	public void setDataSource() {
		//setDataSource(dbms);
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(getDriverClassName());
		bds.setUrl(getUrl());
		bds.setUsername(dbinfo.getUser());
		bds.setPassword(dbinfo.getPassword());
		bds.setMaxActive(Persist.getIntProperty("MaxActive"));
		bds.setMaxIdle(Persist.getIntProperty("MaxIdle"));
		bds.setMaxWait(Persist.getIntProperty("MaxWait"));
		setDataSource(bds);

		factory = new DataSourceConnectionFactory(ds);
		if(checked && logger.isInfoEnabled())
			logger.info("DataSource: " + ds);
	}
	private void setDataSource(DataSource ds){
		this.ds = ds;
	}
}
