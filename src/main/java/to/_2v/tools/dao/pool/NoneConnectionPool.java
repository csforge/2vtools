package to._2v.tools.dao.pool;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.Persist;

public class NoneConnectionPool extends ConnectionPool {
	private static final Log logger = LogFactory.getLog(NoneConnectionPool.class);
	public NoneConnectionPool(MetadataInfo dbinfo) {
		super(dbinfo);
	}
	
	@Override
	public Connection getConnection(){
		try {
			if (dbinfo.getDataSource() != null && dbinfo.getDataSource().length()>0) {
				setMetadataInfoByDSN();
			} else if (dbinfo.getOdbcPath() != null) {
				setMetadataInfoByPath();
			}
			
			Class.forName(getDriverClassName());
			return DriverManager.getConnection(getUrl(), getUser(), getPassword());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	private void setMetadataInfoByDSN() {
		setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
		setUrl("jdbc:odbc:" + dbinfo.getDataSource());
	}
	
	private void setMetadataInfoByPath() {
		setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
		
		String url = null;
		if (dbinfo.getDatabaseType().equals(Persist.ACCESS)) {
			url = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
				+ dbinfo.getOdbcPath() ;
		} else if (dbinfo.getDatabaseType().equals(Persist.EXCEL)) {
			url = "jdbc:odbc:DRIVER=Microsoft Excel Driver (*.xls);DBQ="
					+ dbinfo.getOdbcPath() + ";readonly=false";
		} else if (dbinfo.getDatabaseType().equals(Persist.DBASE)) {
			url = "jdbc:odbc:DRIVER=Microsoft dBase Driver (*.dbf);DBQ="
				+ dbinfo.getOdbcPath() + ";readonly=false";
		} else if (dbinfo.getDatabaseType().equals(Persist.VFP)) {
			url = "jdbc:odbc:DRIVER={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB="
					+ dbinfo.getOdbcPath() + ";Exclusive=Yes";
		}
		setUrl(url);
	}
	
	@Override
	public void setDataSource() {
		// TODO Auto-generated method stub
		
	}

}
