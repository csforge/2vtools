package to._2v.tools.dao;

import java.sql.Connection;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.pool.ApacheConnectionPool;
import to._2v.tools.dao.pool.C3P0ConnectionPool;
import to._2v.tools.dao.pool.ConnectionPool;
import to._2v.tools.dao.pool.JNDIConnectionPool;
import to._2v.tools.dao.pool.ODBCConnectionPool;
import to._2v.tools.dao.pool.OracleConnectionPool;

/**
 * 
 * @author jerry2
 *
 */
public class ServiceLocator {

	private static ServiceLocator instance = null;
	private ConnectionPool pool = null;
	private MetadataInfo dbinfo = null;
	protected boolean checked = true;
	private static boolean showSql = false;
	private static Hashtable locators = new Hashtable();
	
	private static final Log logger = LogFactory.getLog(ServiceLocator.class);
	
	private ServiceLocator(){
	}
	synchronized public static ServiceLocator getInstance() {
		if (instance == null) {
			instance = new ServiceLocator();
			lazy();
		}
		if(logger.isDebugEnabled())
			logger.debug("ServiceLocator instance: "+instance);
		return instance;
	}
	synchronized public static ServiceLocator getInstance(String ds) {
		if (ds == null) {
			return getInstance();
		}
		if(logger.isDebugEnabled())
			logger.debug("DataSource: "+ds);
		ServiceLocator _instance = (ServiceLocator)locators.get(ds);
		if(logger.isDebugEnabled())
			logger.debug("ServiceLocator instance: "+_instance);
		return _instance;
	}
	synchronized public static void addServiceLocator(String ds,MetadataInfo dbinfo){
		ServiceLocator _instance = new ServiceLocator();
		_instance.setDataSource(dbinfo);
		locators.put(ds, _instance);
	}
	
	public void setDataSource(MetadataInfo dbinfo){
		this.dbinfo = dbinfo;
		if(dbinfo.getConnectionPool().equals(Persist.JNDI)){
			pool = new JNDIConnectionPool(dbinfo);
		}else if(dbinfo.getConnectionPool().equals(Persist.ORACLE)){
			pool = new OracleConnectionPool(dbinfo);
		}else if(dbinfo.getConnectionPool().equals(Persist.ODBC)){
			pool = new ODBCConnectionPool(dbinfo);
		}else if(dbinfo.getConnectionPool().equals(Persist.APACHE)){
			pool = new ApacheConnectionPool(dbinfo);
		}else if(dbinfo.getConnectionPool().equals(Persist.C3P0)){
			pool = new C3P0ConnectionPool(dbinfo);
		}else{
			return;
		}
		pool.setDataSource();
		if(checked && logger.isInfoEnabled())
			logger.info(pool);
		showSql = Persist.getBooleanProperty(Persist.SHOW_SQL);
	}
	
	public Connection getConnection() {
		return pool.getConnection();
	}
	
	private static void lazy(){
		if (!Persist.isReady() || getInstance().getDbinfo() == null) {
			if(logger.isInfoEnabled())
				logger.info("lazy ...");
			Persist.configure("persist.properties");
			getInstance().setDataSource(new MetadataInfo(Persist.getProperty(Persist.DBCP), 
					Persist.getProperty(Persist.DataSource), Persist.getProperty(Persist.DriverName), 
					Persist.getProperty(Persist.JdbcURL), Persist.getProperty(Persist.User), 
					Persist.getProperty(Persist.Password)));
		}
	}
	public static boolean isShowSql() {
		return showSql;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public MetadataInfo getDbinfo() {
		return dbinfo;
	}
	
}
