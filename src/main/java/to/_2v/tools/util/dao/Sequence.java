package to._2v.tools.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.dao.DAOUtil;

/**
 * 
 * @author jerry
 *
 */
public class Sequence extends DAOUtil {
	private static final Log logger = LogFactory.getLog(Sequence.class);
	final static String create_table_sql = "CREATE TABLE sys_sequence (seq varchar(45) NOT NULL"
			+ ", s_order tinyint(1) NOT NULL default '1' COMMENT '1:ascending ; 0:descending'"
			+ ", s_value bigint(20) NOT NULL default '0'"
			+ ", PRIMARY KEY  (seq))";
	private static String select_sql;
	private static String current_sql;
//	private final static String update_sql;
	private static String update_asc_sql;	
	private static String update_desc_sql;
	private static String insert_sql;

	private static Sequence _instance;
/*
 * DROP TABLE IF EXISTS `sys_sequence`;
CREATE TABLE `sequence` (
  `seq` varchar(45) NOT NULL,
  `s_order` tinyint(1) NOT NULL default '1' COMMENT '1:ascending ; 0:descending',
  `s_value` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


for T-SQL:

CREATE PROCEDURE proc_seq_id
 @SEQ varchar(50)
AS
update sys_sequence set s_value=s_value+1 where seq=@SEQ
select s_value from sys_sequence where seq=@SEQ
GO

exec proc_seq_id 'EVENT_SMS_ID'

 */
	private Sequence() {
		setShowSQL(false);
		initSQL("");
	}
	private Sequence(String prefix) {
		setShowSQL(false);
		initSQL(prefix+"_");
	}
	void initSQL(String table){
		select_sql = "select s_order,s_value from "+table+"sys_sequence where seq=?";
		current_sql = "select s_value from "+table+"sys_sequence where seq=?";
//		update_sql = "update "+table+"sys_sequence set s_value=? where seq=?";
		update_asc_sql = "update "+table+"sys_sequence set s_value=s_value+1 where seq=?";	
		update_desc_sql = "update "+table+"sys_sequence set s_value=s_value-1 where seq=?";
		insert_sql = "insert into "+table+"sys_sequence(seq,s_order,s_value) values(?,?,?)";
	}
	synchronized static Sequence getInstance() {	
		if (_instance == null) {
			_instance = new Sequence();
		}
		return _instance;
	}

	public static int create(String seq, boolean asc) {
		return getInstance().exeUpdate(insert_sql,
				new Object[] { seq, new Boolean(asc), new Long(0) });
	}
	
	public static long getCurrent(String seq) {
		return getInstance().getLongValue(current_sql, new Object[] { seq });
	}
	
	synchronized public static long getNext(String seq) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = null;
		long c = 0;
		boolean asc = true;
		try {
			conn = getInstance().getConnection();
			conn.setAutoCommit(false);

			psmt = getInstance().getPreparedStatement(conn, select_sql);
			psmt.setObject(1, seq);
			rs = psmt.executeQuery();
			while (rs.next()) {
				asc = rs.getBoolean(1);
				c = rs.getLong(2);
				break;
			}
			if (asc) {
				sql = update_asc_sql;
				c++;
			} else {
				sql = update_desc_sql;
				c--;
			}
			psmt = getInstance().getPreparedStatement(conn, sql);
			psmt.setObject(1, seq);
			psmt.executeUpdate();

			conn.commit();
		} catch (SQLException e) {
			logger.error("Rollback: " + e.getMessage());
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
				logger.error("can't rollback. " + e.getMessage());
			}
		} finally {
			close(psmt);
			close(conn);
		}
		return c;
	}

	protected void rollback() {
		// TODO Auto-generated method stub
	}
}
