package to._2v.tools.dao.concrete;

import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.SingleBean;

public class DatabaseBean extends SingleBean {
	public DatabaseBean(){
		super();
	}
	/**
	 * 
	 * @param dsn
	 */
	public DatabaseBean(String dsn){
		super(dsn);
	}
	/**
	 * 
	 * @param dbms
	 * @param path
	 */
	public DatabaseBean(String dbms,String path){
		super(dbms,path);
	}
	public DatabaseBean(MetadataInfo dbInfo){
		super(dbInfo);
	}
}
