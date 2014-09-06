package to._2v.tools.util.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.DAOUtil;


public class SerialCodeUtil extends DAOUtil {
	private static final Log logger = LogFactory.getLog(SerialCodeUtil.class);
	private final static String create_table_sql = "CREATE TABLE sys_serial_code (spec varchar(45) NOT NULL default ''"
			+ ", prefix varchar(45) NOT NULL default '', count int unsigned NOT NULL default 0"
			+ ", PRIMARY KEY  (spec,prefix))";
	private final static String select_sql = "select count from sys_serial_code where spec=? and prefix=?";
	private final static String update_sql = "update sys_serial_code set count=? where spec=? and prefix=?";
	private final static String insert_sql = "insert into sys_serial_code(spec,prefix,count) values(?,?,?)";

	private static SerialCodeUtil _instance;
/*
DROP TABLE IF EXISTS `sys_serial_code`;
CREATE TABLE  `sys_serial_code` (
  `spec` varchar(45) NOT NULL default '',
  `prefix` varchar(45) NOT NULL default '',
  `count` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`spec`,`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
	private SerialCodeUtil() {
		setDebug(false);
	}

	public static SerialCodeUtil getInstance() {
		if (_instance == null) {
			_instance = new SerialCodeUtil();
//			exists();
		}

		return _instance;
	}

	static boolean exists() {
		int c = getInstance().exeUpdate(create_table_sql);
		if (c > 0)
			return true;
		if(logger.isInfoEnabled())
			logger.info("CREATE TABLE `serial_code`: " + c);
		return false;
	}

	/**
	 * 
	 * @param spec
	 * @param prefix
	 * @return
	 */
	public static int getCount(String spec, String prefix) {
		int cur = 0;
		cur = getInstance().getIntValue(select_sql,
				new Object[] { spec, prefix });
		if (cur < 0)
			cur++;
		return cur;
	}

	/**
	 * get a sequence start with 'prefix'
	 * 
	 * @param spec
	 * @param prefix
	 * @param length
	 * @return
	 */
	public static String getNextCode(String spec, String prefix, int length) {
		StringBuffer sb = new StringBuffer();
		int cur = getCount(spec, prefix);
		if (cur++ > 0) {
			getInstance().exeUpdate(update_sql,
					new Object[] { new Integer(cur), spec, prefix });
		} else {
			getInstance().exeUpdate(insert_sql,
					new Object[] { spec, prefix, new Integer(cur) });
		}
		sb.append(cur);
		int curlen = String.valueOf(cur).length();
		int dec = length - curlen;
		for (int i = 0; i < dec; i++)
			sb.insert(0, "0");
		return prefix + sb.toString();
	}

	protected void rollback() {
		// TODO Auto-generated method stub
	}
}
