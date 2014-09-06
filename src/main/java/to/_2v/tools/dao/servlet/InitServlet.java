package to._2v.tools.dao.servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import to._2v.tools.Config;
import to._2v.tools.Constant;
import to._2v.tools.GlobalVariable;
import to._2v.tools.dao.MetadataInfo;
import to._2v.tools.dao.Persist;
import to._2v.tools.dao.ServiceLocator;
import to._2v.tools.util.StringUtil;


public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = -145178694022646238L;
	Log log;
	String rootPath;
	/**
	 * 
	 * 
	 * 
	 * <servlet>
	 *   <servlet-name>InitServlet</servlet-name>
	 *   <servlet-class>com._2v.tools.dao.servlet.InitServlet</servlet-class>
	 *   
	 *   <init-param>
	 *   	<param-name>log4j-init-file</param-name>
	 *   	<param-value>WEB-INF/log4j.properties</param-value>
	 *   </init-param>
	 *   <init-param>
     *		<param-name>persist-file</param-name>
     *	    <param-value>WEB-INF/persist.properties</param-value>
     *	 </init-param>
	 *   <init-param>
     *		<param-name>config-object-file</param-name>
     *	    <param-value>WEB-INF/config.properties</param-value>
     *	 </init-param>
	 *   <load-on-startup>1</load-on-startup>
	 * </servlet> 
	 * 
	 */
	public void init() throws ServletException {
		System.out.println("[INFO] InitServlet is run......");
		log = LogFactory.getLog(this.getClass());
		rootPath = getServletContext().getRealPath("/");
		GlobalVariable.setProperty(Constant.ROOT_PATH,rootPath);
		try {
			String logfile = getInitParameter("log4j-init-file");
			if (StringUtil.isNotBlank(logfile)) {
				PropertyConfigurator.configure(rootPath + logfile);
				log.info(rootPath + logfile);
			} else {
				File file = new File(rootPath, "WEB-INF" + File.separator + "log4j.properties");
				if (file.exists())
					PropertyConfigurator.configure(file.getPath());
			}
//			 else
//				BasicConfigurator.configure();
			
			String persistfile = getInitParameter("persist-file");
			if (StringUtil.isNotBlank(persistfile)) {
				Persist.configure(new File(rootPath, persistfile));
				log.info(rootPath + persistfile);
			} else {
				File file = new File(rootPath, "WEB-INF" + File.separator + "persist.properties");
				if (file.exists())
					Persist.configure(file);
				else
					Persist.configure();
			}
			//set default DataSource
			ServiceLocator.getInstance().setDataSource(new MetadataInfo(Persist.getProperty(Persist.DBCP), 
					Persist.getProperty(Persist.DataSource), Persist.getProperty(Persist.DriverName), 
					Persist.getProperty(Persist.JdbcURL), Persist.getProperty(Persist.User), 
					Persist.getProperty(Persist.Password)));
			
			String configfile = getInitParameter("config-object-file");
			if (StringUtil.isNotBlank(configfile)) {
				Config.setProperties(new File(rootPath, configfile));
				log.info(rootPath + configfile);
			} else {
				File file = new File(rootPath, "WEB-INF" + File.separator + "config.properties");
				if (file.exists())
					Config.configure(file);
				else
					Config.configure();
			}
			
			configInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	void configInit(){
		if(Config.isReady()){
//			GlobalVariable.setProperty(Constant.IPAGE, Config.getProperty("perPage"));
			if(Config.getProperty("perPage")!=null)
				GlobalVariable.set(Constant.IPAGE, new Integer(Config.getProperty("perPage")));
			else
				GlobalVariable.set(Constant.IPAGE, new Integer(10));
		}
		
		if(Persist.isReady()){
			if(StringUtil.isNotBlank(Persist.getProperty(Persist.SQLStatement_PATH))){
				File file = new File(rootPath, "WEB-INF" + File.separator + Persist.getProperty(Persist.SQLStatement_PATH));
				if (file.exists())
					Persist.setSQLProperties(file);
			} else{
					Persist.configureSQLProperties("SQLStatement.properties");
			}
		}
		
	}

	/*
	 
	  <servlet>
	    <servlet-name>InitServlet</servlet-name>
	    <servlet-class>com._2v.tools.dao.servlet.InitServlet</servlet-class>
	    
	    <init-param>
      		<param-name>log4j-init-file</param-name>
      		<param-value>WEB-INF/log4j.properties</param-value>
    	</init-param>
    	<init-param>
      		<param-name>persist-file</param-name>
      		<param-value>WEB-INF/persist.properties</param-value>
    	</init-param>
    	<init-param>
      		<param-name>config-object-file</param-name>
      		<param-value>WEB-INF/config.properties</param-value>
    	</init-param>
    
	    <load-on-startup>1</load-on-startup>
	 </servlet> 
	 
	 */
}
