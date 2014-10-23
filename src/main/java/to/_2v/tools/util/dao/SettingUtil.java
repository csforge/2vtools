package to._2v.tools.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.DAOUtil;


public class SettingUtil extends DAOUtil {
	private static final Log logger = LogFactory.getLog(SettingUtil.class);
	private final static String create_table_sql = "CREATE TABLE sys_setting (sys varchar(45) NOT NULL default ''"
			+ ", name varchar(45) NOT NULL default '', value varchar(100) NOT NULL default ''"
			+ ", PRIMARY KEY  (sys,name))";
	private final static String find_sql = "select value from sys_setting";
	private final static String select_sql = "select * from sys_setting where sys=?";
	private final static String update_sql = "update sys_setting set value=? where sys=? and name=?";
	private final static String insert_sql = "insert into sys_setting(sys,name,value) values(?,?,?)";
	private static volatile SettingUtil _instance;

	public static SettingUtil getInstance() {
		if (_instance == null) {
			synchronized (SettingUtil.class) {
				if (_instance == null)
					_instance = new SettingUtil();
				// exists();
			}
		}

		return _instance;
	}

	static boolean exists() {
		int c = getInstance().exeUpdate(create_table_sql);
		if (c > 0)
			return true;
		if(logger.isInfoEnabled())
			logger.info("CREATE TABLE `sys_setting`: " + c);
		return false;
	}

	public static String getValue(String sys, String name) {
		String cur = "";
		cur = getInstance().getStringValue(
				find_sql + " where sys=? and name=? ",
				new Object[] { sys, name });
		return cur;
	}

	public static Map getValues(String sys) {
		Connection conn = getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		Map map = new HashMap();
		try {
			psmt = getInstance().getPreparedStatement(conn, select_sql);
			getInstance()
					.setPreparedStatementValues(psmt, new Object[] { sys });
			rs = psmt.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("name"), rs.getString("value"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			close(rs);
			close(psmt);
			close(conn);
		}
		return map;
	}

	public static int insert_updateValue(String sys, Map m) {
		Set set = getValues(sys).keySet();
		Iterator ite = m.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			if (set.contains(key)) {
				getInstance().exeUpdate(update_sql,
						new Object[] { sys, key, m.get(key) });
			} else {
				getInstance().exeUpdate(insert_sql,
						new Object[] { sys, key, m.get(key) });
			}
		}
		return 1;
	}

	protected void rollback() {
		// TODO Auto-generated method stub
	}

}
