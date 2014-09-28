package to._2v.tools.dao;

public class MetadataInfo {

	private String dataSource;
	private String connectionPool;
	private String databaseType;
	private String driverName;
	private String jdbcUrl;
	private String user;
	private String password;
	private String schema;
	private String host;
//	private String port;
	private String driverType;
	private String properties;
	
	private String odbcPath;
	
	/**
	 * use JNDI DataSource
	 * @param dataSource
	 */
	public MetadataInfo(String dataSource) {
		this.connectionPool = "jndi";
		this.setDataSource(dataSource);
	}
	/**
	 * 
	 * @param connectionPool
	 * @param dataSource
	 */
	public MetadataInfo(String connectionPool, String dataSource) {
		this.connectionPool = connectionPool;
		this.setDataSource(dataSource);
	}
	/**
	 * 
	 * @param connectionPool
	 * @param driverName
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 */
	public MetadataInfo(String connectionPool, String driverName,
			String jdbcUrl, String user, String password) {
		this.connectionPool = connectionPool;
		this.driverName = driverName;
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}
	/**
	 * 
	 * @param connectionPool
	 * @param dataSource if(connectionPool=='jndi' or 'odbc')
	 * @param driverName
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 */
	public MetadataInfo(String connectionPool, String dataSource,
			String driverName, String jdbcUrl, String user, String password) {
		this.connectionPool = connectionPool;
		this.setDataSource(dataSource);
		this.driverName = driverName;
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}
	/**
	 * 
	 * @param connectionPool
	 * @param dataSource
	 * @param driverType
	 * @param host
	 * @param schema
	 * @param user
	 * @param password
	 * @param properties
	 */
	public MetadataInfo(String connectionPool, String dataSource,
			String driverType, String host, String schema, String user,
			String password, String properties) {
		this.connectionPool = connectionPool;
		this.setDataSource(dataSource);
		this.driverType = driverType;
		this.host = host;
		this.schema = schema;
		this.user = user;
		this.password = password;
		this.properties = properties;
		arrange();
	}
	private void arrange(){
		if(driverType.equals(Persist.ORACLE)){
			driverName = "oracle.jdbc.driver.OracleDriver";
			//jdbc:oracle:thin:@<host>:<port>:<database>;
			jdbcUrl = "jdbc:oracle:thin:@" + host + ":1521:" + schema;
		}else if(driverType.equals(Persist.MYSQL)){
			driverName = "com.mysql.jdbc.Driver";
			//jdbc:mysql://<host>:<port>/<database>[?propertyName1][=propertyValue1][&propertyName2]...
			jdbcUrl = "jdbc:mysql://" + host + ":3306/" + schema;
		}else if(driverType.equals(Persist.SQLSERVER)){
			driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
			//jdbc:sqlserver://<hostname>:1433;databaseName=<dbname>[;property=value...]
			jdbcUrl = "jdbc:sqlserver://" + host + ":1433;databaseName=" + schema;
		}else if(driverType.equals(Persist.DB2)){
			driverName = "COM.ibm.db2.jdbc.net.DB2Driver";
			//jdbc:db2://<hostname>:6789/<dbname>
			jdbcUrl = "jdbc:db2://" + host + ":6789/" + schema;
		}else if(driverType.equals(Persist.SYBASE)){
			driverName = "com.sybase.jdbc3.jdbc.SybDriver";//jconn3.jar
			//jdbc:sybase:Tds:<host>:<port>/<database>
			jdbcUrl = "jdbc:sybase:Tds:" + host + ":7100/" + schema;
		}else if(driverType.equals(Persist.POSTGRESQL)){
			driverName = "org.postgresql.Driver";
			//jdbc:postgresql://<hostname>:5432/<dbname>
			jdbcUrl = "jdbc:postgresql://" + host + ":5432/" + schema;
		}else if(driverType.equals(Persist.HSQLDB)){
			driverName = "org.hsqldb.jdbcDriver";
			//jdbc:hsqldb:<dbname>
			jdbcUrl = "jdbc:hsqldb:" + schema;
		}else if(driverType.equals(Persist.jTDS_SQLSERVER)){
			driverName = "net.sourceforge.jtds.jdbc.Driver";
			//jdbc:jtds:<server_type>://<host>[:<port>][/<database>][;<property>=<value>[;...]]
			jdbcUrl = "jdbc:jtds:sqlserver://" + host + ":1433/" + schema;
		}else if(driverType.equals(Persist.jTDS_SYBASE)){
			driverName = "net.sourceforge.jtds.jdbc.Driver";
			//jdbc:jtds:<server_type>://<host>[:<port>][/<database>][;<property>=<value>[;...]]
			jdbcUrl = "jdbc:jtds:sybase://" + host + ":7100/" + schema;
		}else if(driverType.equals(Persist.HXTT_EXCEL)){
			driverName = "com.hxtt.sql.excel.ExcelDriver";
			//jdbc:excel:[//]/[DatabasePath][?prop1=value1[;prop2=value2]] (You can omit that "//" characters sometimes)
			jdbcUrl = "jdbc:excel:///" + schema;
		}else if(driverType.equals(Persist.HXTT_DBF)){
			driverName = "com.hxtt.sql.dbf.DBFDriver";
			//jdbc:dbf:[//]/[DatabasePath][?prop1=value1[;prop2=value2]] (You can omit that "//" characters sometimes)
			jdbcUrl = "jdbc:dbf:///" + schema;
		}else if (driverType.equals(Persist.DSN)) {
			driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
			jdbcUrl = "jdbc:odbc:" + schema;
		}else if (driverType.equals(Persist.ACCESS)) {
			driverName="sun.jdbc.odbc.JdbcOdbcDriver";
			jdbcUrl = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
				+ schema ;
		}else if (driverType.equals(Persist.EXCEL)) {
			driverName="sun.jdbc.odbc.JdbcOdbcDriver";
			jdbcUrl = "jdbc:odbc:DRIVER=Microsoft Excel Driver (*.xls);DBQ="
				+ schema + ";readonly=false";
		}else if (driverType.equals(Persist.DBASE)) {
			driverName="sun.jdbc.odbc.JdbcOdbcDriver";
			jdbcUrl = "jdbc:odbc:DRIVER=Microsoft dBase Driver (*.dbf);DBQ="
				+ schema + ";readonly=false";
		}else if (driverType.equals(Persist.VFP)) {
			driverName="sun.jdbc.odbc.JdbcOdbcDriver";
			jdbcUrl = "jdbc:odbc:DRIVER={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB="
				+ schema + ";Exclusive=Yes";
		}
		
		if(properties!=null)
			jdbcUrl = jdbcUrl + properties;
	}
	public String getDatabaseType() {
		return databaseType;
	}
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverType() {
		return driverType;
	}
	public void setDriverType(String driverType) {
		this.driverType = driverType;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		if(dataSource!=null && dataSource.length()==0)
			this.dataSource = null;
		else
			this.dataSource = dataSource;
	}
	public String getConnectionPool() {
		return connectionPool;
	}
	public void setConnectionPool(String connectionPool) {
		this.connectionPool = connectionPool;
	}
	public String getOdbcPath() {
		return odbcPath;
	}
	public void setOdbcPath(String odbcPath) {
		this.odbcPath = odbcPath;
	}

}
