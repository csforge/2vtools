package to._2v.tools.dao.pool;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;

public abstract class ConnectionPool {
	private static final Log logger = LogFactory.getLog(ConnectionPool.class);
	public abstract Connection getConnection();
	public abstract void setDataSource();
	protected MetadataInfo dbinfo;
	protected boolean checked = true;
	
	public ConnectionPool(MetadataInfo dbinfo){
		super();
		this.dbinfo = dbinfo;
	}
	protected void setDriverClassName(String driverName) {
		dbinfo.setDriverName(driverName);
	}
	protected String getDriverClassName(){
		String value = dbinfo.getDriverName();
		if(checked && logger.isDebugEnabled())
			logger.debug(value);
		return value;
	}
	protected void setUrl(String jdbcUrl) {
		dbinfo.setJdbcUrl(jdbcUrl);
	}
	protected String getUrl(){
		String value = dbinfo.getJdbcUrl();
		if(checked && logger.isDebugEnabled())
			logger.debug(value);
		return value;
	}
	protected String getUser(){
		String value = dbinfo.getUser();
//		if(checked && logger.isInfoEnabled())
//			logger.info(value);
		return value;
	}
	protected String getPassword(){
		String value = dbinfo.getPassword();
//		if(checked && logger.isInfoEnabled())
//			logger.info(value);
		return value;
	}
	public MetadataInfo getDbinfo() {
		return dbinfo;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
