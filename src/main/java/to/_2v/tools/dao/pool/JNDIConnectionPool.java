package to._2v.tools.dao.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;


public class JNDIConnectionPool extends ConnectionPool {
	private static final Log logger = LogFactory.getLog(JNDIConnectionPool.class);
//	private JNDIConnectionPool instance = null;
	private DataSource ds;
	public JNDIConnectionPool(MetadataInfo dbinfo){
		super(dbinfo);
	}
	
//	public static JNDIConnectionPool getInstance(MetadataInfo dbinfo) {
//		if (instance == null) {
//			instance = new JNDIConnectionPool(dbinfo);
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

	public void setDataSource() {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource)envContext.lookup(dbinfo.getDataSource());
			
			setDataSource(ds);
			if(checked && logger.isInfoEnabled())
				logger.info("DataSource: " + this.ds + "; DS Name: " +dbinfo.getDataSource());
		} catch (NamingException e) {
			logger.error(e.getMessage());
		}
	}

}
