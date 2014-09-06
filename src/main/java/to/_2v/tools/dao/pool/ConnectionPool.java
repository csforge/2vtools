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
	
	protected String getDriverClassName(){
		String driver = dbinfo.getDriverName();
		if(checked && logger.isInfoEnabled())
			logger.info(driver);
		return driver;
	}
	protected String getUrl(){
		String url = dbinfo.getJdbcUrl();
		if(checked && logger.isInfoEnabled())
			logger.info(url);
		return url;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
