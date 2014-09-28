package to._2v.tools.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.util.Configurator;


public class Persist {
	private static Log logger = LogFactory.getLog(Persist.class);
	public final static String DEFAULT_FILE = "persist.properties";
	public final static String ACCESS = "access";
	public final static String EXCEL = "excel";
	public final static String DBASE = "dbase";
	public final static String VFP = "vfp";//Visual FoxPro
	public final static String DSN = "dsn";
	
	public final static String ORACLE = "oracle";
	public final static String SQLSERVER = "sqlserver";
	public final static String MYSQL = "mysql";
	public final static String DB2 = "db2";
	public final static String POSTGRESQL = "postgresql";
	public final static String HSQLDB = "hsqldb";
	public final static String SYBASE = "sybase";
	
	public final static String jTDS_SQLSERVER = "jtds_sqlserver";
	public final static String jTDS_SYBASE = "jtds_sybase";
	public final static String HXTT_DBF = "HXTT_DBF";
	public final static String HXTT_EXCEL = "HXTT_Excel";
	
	public final static String User = "user";
	public final static String Password = "password";
	public final static String JdbcURL = "jdbcUrl";
	public final static String DriverName = "driverName";
	public final static String DatabaseType = "databaseType";
	public final static String Suffix = "suffix";
	
	public final static String DBCP = "dbcp";
	public final static String DataSource = "dataSource";
	public final static String ODBC = "odbc";
	public final static String ODBC_PATH = "odbc_path";
	public final static String SQLStatement_PATH = "sqlStatement_path";
	
	public final static String APACHE = "apache";
	public final static String JNDI = "jndi";
	public final static String C3P0 = "c3p0";
	public final static String NONE = "none";
	
	public final static String SQL_KEY = "SqlKey";
	public final static String SQL_STATEMENT = "SqlStatement";
	public final static String SHOW_SQL = "show_sql";
	
	private static Properties PROPS = new Properties();
	private static Properties SQLPROPS = new Properties();

	private static boolean ready = false;
	private static boolean sql_ready = false;
	public static boolean isReady() {
		return ready;
	}

	public static void configure(){
		configure("persist.properties");
	}
	
	public static void configure(String resource){
		InputStream is = Configurator.getResourceAsStream(resource, Persist.class);
		try {
			if (is != null){
				PROPS.load(is);
				ready = true;
			}
			else{
				if(logger.isInfoEnabled())
					logger.info("Cannot find the '" + resource + "' in classpath.");
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	public static void configure(File file){
		try {
			if (file != null){
				PROPS.load(new FileInputStream(file));
				ready = true;
			}
		} catch (FileNotFoundException e) {
			logger.warn("Cannot find the file: " +file,e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static Properties getProperties() {
		return PROPS;
	}

	public static String getProperty(String key) {
		try {
			return PROPS.getProperty(key).trim();
		} catch (Exception e) {
			logger.error("cannot find key:"+key+" -"+e.getMessage());
			return null;
		}
	}
	
	public static boolean isNotBlank(String key) {
		if (PROPS.getProperty(key) == null || PROPS.getProperty(key).equals(""))
			return false;
		return true;
	}

	public static int getIntProperty(String key) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	public static boolean getBooleanProperty(String key) {
		try {
			if (getProperty(key).equals("1") || getProperty(key).equals("true"))
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	public static Properties getSQLProperties() {
		if(!sql_ready){
			configureSQLProperties(getProperty(SQLStatement_PATH));
		}
		return SQLPROPS;
	}
	public static void configureSQLProperties(String resource) {
		InputStream is = Configurator.getResourceAsStream(resource, Persist.class);
		try {
			if (is != null){
				SQLPROPS.load(is);
				sql_ready = true;
				if(logger.isInfoEnabled())
					logger.info("load SQLStatement in classpath: "+resource);
			} else{
				if(logger.isInfoEnabled())
					logger.info("Cannot find the '" + resource + "' in classpath.");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public static void setSQLProperties(File file) {
		try {
			SQLPROPS.load(new FileInputStream(file));
			sql_ready = true;
			if(logger.isInfoEnabled())
				logger.info("load SQLStatement in : "+file.getPath());
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static String getSQLString(String key) {
		if(!sql_ready){
			configureSQLProperties(getProperty(SQLStatement_PATH));
		}
		return SQLPROPS.getProperty(key);
	}
}
