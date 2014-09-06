package to._2v.tools.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.RowList;
import to._2v.tools.RowMap;
import to._2v.tools.util.ReflectUtil;
import to._2v.tools.util.StringUtil;

/**
 * 
 * @author jerry2
 *
 */
public abstract class DAOUtil {

	private static final Log logger = LogFactory.getLog(DAOUtil.class);
	protected ServiceLocator serviceLocator;
	abstract protected void rollback();
	private boolean checked = false;
	private boolean debug = false;
	private boolean showSQL = true;
	
	private boolean showSQL() {
		if (showSQL)
			return ServiceLocator.isShowSql();
		else
			return false;
	}
	
	protected DAOUtil() {
		serviceLocator = ServiceLocator.getInstance();
	}

	protected DAOUtil(boolean checked) {
		this.checked = checked;
		serviceLocator = ServiceLocator.getInstance();
	}

	protected DAOUtil(String ds) {
		serviceLocator = ServiceLocator.getInstance(ds);
	}

	protected DAOUtil(String ds, boolean checked) {
		this.checked = checked;
		serviceLocator = ServiceLocator.getInstance(ds);
	}
	/**
	 * 
	 * @param ds
	 * @param prefix the key name prefix in <em>persist.properties</em> file, like "ext_", don't includes "jdbcUrl"...
	 */
	public static void setDataSource(String ds, String prefix) {
		ServiceLocator.addServiceLocator(ds, new MetadataInfo(Persist.getProperty(prefix+Persist.DBCP), 
				Persist.getProperty(prefix+Persist.DriverName), Persist.getProperty(prefix+Persist.JdbcURL), 
				Persist.getProperty(prefix+Persist.User), Persist.getProperty(prefix+Persist.Password)));
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void setShowSQL(boolean showSQL) {
		this.showSQL = showSQL;
	}

	protected Connection getConnection() {
		return serviceLocator.getConnection();
	}
	/**
	 * 
	 * @param table Querys
	 * @return
	 */
	protected int exeUpdateTransaction(List table, String rollback) {
		Connection conn = null;
		PreparedStatement psmt = null;
		Object[] values = null;
		int c = 0, grp = -1;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			
			Iterator it = table.iterator();
			while (it.hasNext()) {
				Query entry = (Query) it.next();
				values = entry.getValues();
				if (grp != entry.getGroup())
					psmt = getPreparedStatement(conn, entry.getSql());
				setPreparedStatementValues(psmt, values);
				c += psmt.executeUpdate();
				grp = entry.getGroup();
			}
			
			conn.commit();
		} catch (SQLException e) {
			logger.error("Rollback: " + e.getMessage());
			logger.error(toString(values));
			try {
				conn.rollback();
				conn.setAutoCommit(true);
				if (rollback == null || rollback.equals(""))
					rollback();
				else
					ReflectUtil.invoke(this, rollback);
			} catch (SQLException e1) {
				logger.error("can't rollback. " + e.getMessage());
			}
		}finally{
			close(psmt);
			close(conn);
		}
		return c;
	}

	protected int exeUpdateTransaction(List table) {
		return this.exeUpdateTransaction(table, null);
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error("can't close connection. "+e.getMessage());
		}
	}
	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			logger.error("can't close preparedStatement. "+e.getMessage());
		}
	}
	public static void close(PreparedStatement psmt) {
		try {
			if (psmt != null) {
				psmt.close();
			}
		} catch (SQLException e) {
			logger.error("can't close preparedStatement. "+e.getMessage());
		}
	}
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			logger.error("cannot close resultSet. "+e.getMessage());
		}
	}
	protected DatabaseMetaData getDatabaseMetaData(Connection conn) throws SQLException {
		return getConnection().getMetaData();
	}

	protected void setPreparedStatementValues(PreparedStatement psmt,List values) throws SQLException{
		if (logger.isDebugEnabled())
			logger.debug(values);
		if (debug && logger.isInfoEnabled())
			logger.info(values);
		
		if (values != null && values.size() > 0) {
			for (int i = 0; i < values.size(); i++) {
				if (checked) {
					values.set(i, timestamp(values.get(i)));
				}
				psmt.setObject(i + 1, values.get(i));
			}
		}
	}
	protected void setPreparedStatementValues(PreparedStatement psmt,Object[] values) throws SQLException{
		if (logger.isDebugEnabled())
			logger.debug(toString(values));
		if (debug && logger.isInfoEnabled())
			logger.info(toString(values));

		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				if (checked) {
					values[i] = timestamp(values[i]);
				}
				psmt.setObject(i + 1, values[i]);
			}
		}
	}
	protected List getTableNames() {
		return getTableNames(getConnection());
	}
	protected List getTableNames(Connection conn) {
		List LL = new RowList();
		DatabaseMetaData dbmd;
		try {
			dbmd = getDatabaseMetaData(conn);
			ResultSet rs = dbmd.getTables("", "", "", null);
			while (rs.next()) {
				String TABLE_NAME = rs.getString("TABLE_NAME");
				LL.add(TABLE_NAME);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return LL;
	}
	protected List getColumnName(String table, int column_type){
		return getColumnName(getConnection(),table,column_type);
	}
	protected List getColumnName(Connection conn, String table, int column_type) {
		List LL = new RowList();
		String sqlstr = "select * from " + table;
		Statement stmt;
		try {
			stmt = conn.createStatement(
//					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE
					);
			ResultSet rs = stmt.executeQuery(sqlstr);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				String column = "";
				if (column_type == -1) {
					column = rsmd.getColumnName(j);
				} else {
					if (column_type == rsmd.getColumnType(j)) {
						column = rsmd.getColumnName(j);
					} else {
						continue;
					}
				}
				LL.add(column);
			}
		} catch (SQLException e) {
			logger.error(table+", "+e.getMessage());
		}
		return LL;
	}

	protected PreparedStatement getPreparedStatement(Connection conn,String sqlstr) {
		if((debug || showSQL()) && logger.isInfoEnabled())
			logger.info(sqlstr);
		PreparedStatement psmt = null;
		try {
			psmt = conn.prepareStatement(sqlstr
//					,ResultSet.TYPE_SCROLL_INSENSITIVE
//					,ResultSet.CONCUR_UPDATABLE
					);
		} catch (Exception e) {
			logger.error("cannot get prepareStatment. "+e.getMessage());
		}
		return psmt;
	}
	
	public List exeSelect(String sqlstr) { //multi- rows and columns
		  return exeSelect(sqlstr, null);
	}
	public List getMultiRows(String sqlstr) { //multi- rows and columns
		  return exeSelect(sqlstr);
	}
	public List getResultList(String sqlstr) { //multi- rows and columns
		  return exeSelect(sqlstr);
	}
	
	public List exeFieldsSelect(String sqlstr) { // single row
		return exeFieldsSelect(sqlstr, null);
	}
	public List getSingleRow(String sqlstr) { // single row
		return exeFieldsSelect(sqlstr);
	}
	public List getResult(String sqlstr) { // single row
		return exeFieldsSelect(sqlstr);
	}
	public List exeRecordsSelect(String sqlstr) { // single column
		return exeRecordsSelect(sqlstr, null);
	}	
	public List getSingleColumn(String sqlstr) { // single column
		return exeRecordsSelect(sqlstr);
	}
	public List getFirstColumnResult(String sqlstr) { // single column
		return exeRecordsSelect(sqlstr);
	}
	
	public Object getResult(String sqlstr, Class clazz, boolean isUnderscore) { // single row
		return getResult(sqlstr, null, clazz, isUnderscore);
	}
	public Object getResult(String sqlstr, Class clazz) {
		return getResult(sqlstr, clazz, false);
	}
	public Object getUnderscoreResult(String sqlstr, Class clazz) { // single row
		return getResult(sqlstr, clazz, true);
	}
	
	public Object getResult(String sqlstr, Object targetObj, boolean isUnderscore) { // single row
		return getResult(sqlstr, null, targetObj, isUnderscore);
	}
	public Object getResult(String sqlstr, Object targetObj) {
		return getResult(sqlstr, targetObj, false);
	}
	public Object getUnderscoreResult(String sqlstr, Object targetObj) { // single row
		return getResult(sqlstr, targetObj, true);
	}
	
	public List getResultList(String sqlstr, Class clazz, boolean isUnderscore)  { // multi - rows and columns
		return getResultList(sqlstr, null, clazz, isUnderscore);
	}
	public List getResultList(String sqlstr, Class clazz)  { 
		return getResultList(sqlstr, clazz, false);
	}
	public List getUnderscoreResultList(String sqlstr, Class clazz)  { // multi - rows and columns
		return getResultList(sqlstr, clazz, true);
	}
	
	public Object getSelectValue(String sqlstr) { // single column
		return getSelectValue(sqlstr, null);
	}
	public int exeUpdate(String sqlstr) {
		return exeUpdate(sqlstr, null);
	}
	public Map fillClumnData(String sqlstr) {
	    return fillClumnData(sqlstr,null);
	}
	public List fillRowsData(String sqlstr) {
		return fillRowsData(sqlstr,null);
	}
	protected ResultSet getRs(String sqlstr) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn,sqlstr);
		ResultSet rs = psmt.executeQuery();
		return rs;
	}
	
	/**
	 * 
	 * @param table tableName
	 * @return
	 * @
	 */
	public int dropTable(String table)  {
		return exeUpdate("DROP TABLE IF EXISTS " + table);
	}
	/**
	 * 
	 * @param sql DDL
	 * @return
	 * @
	 */
	public int createTable(String sql)  {
		return exeUpdate(sql);
	}
	
	public Map fillClumnData(String sqlstr,Object[] values) {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		Map table = fillClumnData(psmt, values);
		close(psmt);
		close(conn);
		return table;
	}
	
	public List fillRowsData(String sqlstr,Object[] values) {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = fillRowsData(psmt, values);
		close(psmt);
		close(conn);
		return results;
	}
	
	protected Map fillClumnData(PreparedStatement psmt,Object[] values) {
		Map table = new RowMap();
		ResultSet rs = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					String column = rsmd.getColumnName(j);
					Object field = rs.getObject(column);
					if (field != null)
						table.put(column, field);
				}
				break;
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally{
			close(rs);
		}
		
		return table;
	}
	
	protected List fillRowsData(PreparedStatement psmt,Object[] values) {
		List results = new RowList();
		ResultSet rs = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while(rs.next()){
				Map table = new RowMap();
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					String column = rsmd.getColumnName(j);
					Object field = rs.getObject(column);
					if (field != null)
						table.put(column, field);
				}
				results.add(table);
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally{
			close(rs);
		}
		
		return results;
	}
	
	public List exeSelect(String sqlstr, Object[] values) { //multi- rows and columns
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = exeSelect(psmt, values);
		close(psmt);
		close(conn);
		return results;
	}
	public List getMultiRows(String sqlstr, Object[] values) { //multi- rows and columns
		return exeSelect(sqlstr, values);
	}
	public List getResultList(String sqlstr, Object[] values) { //multi- rows and columns
		return exeSelect(sqlstr, values);
	}

	public List exeFieldsSelect(String sqlstr, Object[] values) { // single row
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = exeFieldsSelect(psmt, values);
		close(psmt);
		close(conn);
		return results;
	}
	public List getSingleRow(String sqlstr, Object[] values) { // single row
		return exeFieldsSelect(sqlstr, values);
	}
	public List getResult(String sqlstr, Object[] values) { // single row
		return exeFieldsSelect(sqlstr, values);
	}
	
	public List exeRecordsSelect(String sqlstr, Object[] values) { // single column
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = exeRecordsSelect(psmt, values);
		close(psmt);
		close(conn);
		return results;
	}
	public List getSingleColumn(String sqlstr, Object[] values)  { // single column
		return exeRecordsSelect(sqlstr, values);
	}
	public List getFirstColumnResult(String sqlstr, Object[] values)  { // single column
		return exeRecordsSelect(sqlstr, values);
	}

	public List getResultList(String sqlstr, Object[] values, Class clazz, boolean isUnderscore)  { // multi- rows and columns
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		List results = getResultList(psmt, values, clazz, isUnderscore);
		close(psmt);
		close(conn);
		return results;
	}
	public List getResultList(String sqlstr, Object[] values, Class clazz)  { // multi- rows and columns
		return getResultList(sqlstr, values, clazz, false);
	}
	public List getUnderscoreResultList(String sqlstr, Object[] values, Class clazz)  { // multi- rows and columns
		return getResultList(sqlstr, values, clazz, true);
	}
	
	public Object getResult(String sqlstr, Object[] values, Class clazz, boolean isUnderscore) { // single row
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		Object vo = getResult(psmt, values, clazz, isUnderscore);
		close(psmt);
		close(conn);
		return vo;
	}
	public Object getResult(String sqlstr, Object[] values, Class clazz) { // single row
		return getResult(sqlstr, values, clazz, false);
	}
	
	public Object getUnderscoreResult(String sqlstr, Object[] values, Class clazz) { // single row
		return getResult(sqlstr, values, clazz, true);
	}
	
	public Object getResult(String sqlstr, Object[] values, Object targetObj, boolean isUnderscore) { // single row
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		Object vo = getResult(psmt, values, targetObj, isUnderscore);
		close(psmt);
		close(conn);
		return vo;
	}
	public Object getResult(String sqlstr, Object[] values, Object targetObj) { // single row
		return getResult(sqlstr, values, targetObj, false);
	}
	
	public Object getUnderscoreResult(String sqlstr, Object[] values, Object targetObj) { // single row
		return getResult(sqlstr, values, targetObj, true);
	}
	
	public Object getSelectValue(String sqlstr, Object[] values) { // single column
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);
		Object obj = getSelectValue(psmt, values);
		close(psmt);
		close(conn);
		return obj;
	}

	public int exeUpdate(String sqlstr, Object[] values) {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn, sqlstr);

		int c = exeUpdate(psmt, values);
		close(psmt);
		close(conn);
		return c;
	}
	/**
	 * 
	 * @param psmt
	 * @param values
	 * @return List (2-level),first(outer) level:a List within rows,second(inner) level: a List within columns
	 * @
	 */
	protected List exeSelect(PreparedStatement psmt,Object[] values) { //multi- rows and columns
		ResultSet rs = null;
		List RecordsLL = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			RecordsLL = new RowList();

//			rs.beforeFirst();
			while (rs.next()) { //rows --> records
				List FieldsLL = new RowList(); //columns
				for (int i = 1; i <= rsmd.getColumnCount(); i++) { //columns --> fields
					Object field = rs.getObject(i);
					FieldsLL.add(field);
				}
				RecordsLL.add(FieldsLL);
			}
		}catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally {
			close(rs);
		}
		return RecordsLL;
	}
	protected List getMultiRows(PreparedStatement psmt,Object[] values) { //multi- rows and columns
		return exeSelect(psmt, values);
	}
	
	protected List exeFieldsSelect(PreparedStatement psmt,Object[] values)  { // single row
		ResultSet rs = null;
		RowList FieldsLL = null;
		try {
			setPreparedStatementValues(psmt,values);
			rs = psmt.executeQuery();

//			rs.beforeFirst();
			while (rs.next()) {
				FieldsLL = new RowList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) { //columns --> fields
					Object field = rs.getObject(i);
					FieldsLL.add(field);
				}
				break;
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally {
			close(rs);
		}
		return FieldsLL;
	}
	protected List getSingleRow(PreparedStatement psmt,Object[] values)  { // single row
		return exeFieldsSelect(psmt, values);
	}

	protected List exeRecordsSelect(PreparedStatement psmt,Object[] values) { // single column
		ResultSet rs = null;
		RowList RecordsLL = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			RecordsLL = new RowList();

//			rs.beforeFirst();
			while (rs.next()) { //rows --> records
				Object field = rs.getObject(1); //
				RecordsLL.add(field);
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally{
			close(rs);
		}
		return RecordsLL;
	}
	protected List getSingleColumn(PreparedStatement psmt,Object[] values) { // single column
		return exeRecordsSelect(psmt, values);
	}

	protected List getResultList(PreparedStatement psmt,Object[] values,Class clazz,boolean isUnderscore) { //multi- rows and columns
		ResultSet rs = null;
		List results = null;
		try {
			setPreparedStatementValues(psmt,values);
			rs = psmt.executeQuery();
			Map fields = mappingColumnToFields(rs, clazz, isUnderscore);
			
			results = new RowList();
//			rs.beforeFirst();
			while (rs.next()) { // rows --> records
				try {
					Object vo = fillToVO(rs, fields, clazz); // columns --> fields
					results.add(vo);
				} catch (Exception e) {
					logger.warn(psmt.toString());
					logger.error(e.getMessage());			
//					logger.warn(toString(values));
				}
			}
		}catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());
//			logger.warn(toString(values));
		}finally {
			close(rs);
		}
		return results;
	}
	
	protected List getResultList(PreparedStatement psmt,Object[] values,Class clazz) { 
		return getResultList(psmt, values, clazz, false);
	}
	
	protected List getUnderscoreResultList(PreparedStatement psmt,Object[] values,Class clazz) { //multi- rows and columns
		return getResultList(psmt, values, clazz, true);
	}
	
	protected Object getResult(PreparedStatement psmt, Object[] values,
			Class clazz, boolean isUnderscore) { // single row
		ResultSet rs = null;
		Object vo = null;
		try {
			setPreparedStatementValues(psmt,values);
			rs = psmt.executeQuery();
			Map fields = mappingColumnToFields(rs, clazz, isUnderscore);
			
//			rs.beforeFirst();
			while (rs.next()) {
				vo = fillToVO(rs, fields, clazz);
				break;
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
			return null;
		}finally {
			close(rs);
		}
		return vo;
	}
	
	protected Object getResult(PreparedStatement psmt,Object[] values,Class clazz)  { // single row
		return getResult(psmt, values, clazz, false);
	}
	
	protected Object getUnderscoreResult(PreparedStatement psmt,Object[] values,Class clazz)  { // single row
		return getResult(psmt, values, clazz, true);
	}
	
	protected Object getResult(PreparedStatement psmt, Object[] values,
			Object tagetObj, boolean isUnderscore)  { // single row
		if(tagetObj==null)
			return null;
		ResultSet rs = null;
		try {
			setPreparedStatementValues(psmt,values);
			rs = psmt.executeQuery();
			Map fields = mappingColumnToFields(rs, tagetObj.getClass(), isUnderscore);
			
//			rs.beforeFirst();
			while (rs.next()) {
				fillToVO(rs, fields, tagetObj);
				break;
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		}finally {
			close(rs);
		}
		return tagetObj;
	}
	
	protected Object getResult(PreparedStatement psmt,Object[] values,Object tagetObj)  { // single row
		return getResult(psmt, values, tagetObj, false);
	}
	
	protected Object getUnderscoreResult(PreparedStatement psmt,Object[] values,Object tagetObj)  { // single row
		return getResult(psmt, values, tagetObj, true);
	}
	
	protected Object getSelectValue(PreparedStatement psmt,Object[] values) { // single column
		ResultSet rs = null;
		Object field = null;
		try {
			setPreparedStatementValues(psmt,values);
			
			rs = psmt.executeQuery();
			field = null;
//			rs.beforeFirst();
			while (rs.next()) { // rows --> records
				field = rs.getObject(1); // 
				break;
			}
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
		} finally {
			close(rs);
		}
		return field;
	}
	protected int exeUpdate(PreparedStatement psmt,List values) {
		try {
			setPreparedStatementValues(psmt, values);

			return psmt.executeUpdate();
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
			return 0;
		}
	}
	protected int exeUpdate(PreparedStatement psmt,Object[] values) {
		try {
			setPreparedStatementValues(psmt, values);

			return psmt.executeUpdate();
		} catch (SQLException e) {
			logger.warn(psmt.toString());
			logger.error(e.getMessage());			
//			logger.warn(toString(values));
			return 0;
		}
	}
	public String getStringValue(String sqlstr,Object[] values) {
		Object obj = getSelectValue(sqlstr,values);
		if (obj != null)
			return obj.toString();
		else
			return null;
	}
	public Integer getInteger(String sqlstr, Object[] values)  {
		try {
			return (Integer) getSelectValue(sqlstr, values);
		} catch (Exception ex) {
			return new Integer(-1);
		}
	}
	public int getIntValue(String sqlstr,Object[] values) {
		int value = -1;
		String str = getStringValue(sqlstr,values);
		if (str != null) {
			try {
				value = Integer.parseInt(str);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
		return value; 
	}
	public Long getLong(String sqlstr, Object[] values)  {
		try {
			return (Long) getSelectValue(sqlstr, values);
		} catch (Exception ex) {
			return new Long(-1);
		}
	}
	public long getLongValue(String sqlstr,Object[] values) {
		long value = -1;
		String str = getStringValue(sqlstr,values);
		if (str != null) {
			try {
				value = Long.parseLong(str);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
		return value; 
	}
	public Double getDouble(String sqlstr, Object[] values)  {
		try {
			return (Double) getSelectValue(sqlstr, values);
		} catch (Exception ex) {
			return new Double(-1);
		}
	}
	public double getDoubleValue(String sqlstr,Object[] values) {
		double value = -1;
		String str = getStringValue(sqlstr,values);
		if (str != null) {
			try {
				value = Double.parseDouble(str);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
		}
		return value;
	}
	public String getStringValue(String sqlstr) {
		return getStringValue(sqlstr,null); 
	}
	public Integer getInteger(String sqlstr) {
		return getInteger(sqlstr,null); 
	}
	public int getIntValue(String sqlstr) {
		return getIntValue(sqlstr,null); 
	}
	public Long getLong(String sqlstr) {
		return getLong(sqlstr,null); 
	}
	public long getLongValue(String sqlstr) {
		return getLongValue(sqlstr,null); 
	}
	public Double getDouble(String sqlstr) {
		return getDouble(sqlstr,null);
	}
	public double getDoubleValue(String sqlstr) {
		return getDoubleValue(sqlstr,null);
	}
	
	public Object getField(Object table,String name){
		if(table instanceof Hashtable){
			return ((Hashtable)table).get(name);
		}
		return null;
	}
	public Object getRecord(Object list,String name){
		if(list instanceof List){
//			for(int i=0;i<((List)list).size();i++){
//				return ((Hashtable)((List)list).get(i)).get(name);
//			}
			return ((Hashtable)((List)list).get(0)).get(name);
		}
		return null;
	}
	/**
	 * 
	 * @param sql
	 * @param values a List fill with Object[]
	 * @return
	 * @
	 */
	public int update(String sql,List values) {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn,sql);
		int c = 0;
		if (values != null){
			Iterator it = values.iterator();
			while(it.hasNext())
				c += exeUpdate(psmt, (Object[])it.next());
		}
		close(psmt);
		close(conn);
		return c;
	}
	
	public int delete(String sql, Object[] ids)  {
		Connection conn = getConnection();
		PreparedStatement psmt = getPreparedStatement(conn,sql);		
		int c = 0;
		if (ids != null && ids.length > 0)
			for (int i = 0; i < ids.length; i++)
				c += exeUpdate(psmt, new Object[] { ids[i] });
		close(psmt);
		close(conn);
		return c;
	}
	
	protected Object fillToVO(ResultSet rs, Map fields, Class clazz)throws SQLException {
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			logger.error("cannot Instantiation:" + e.getMessage());
		}
		fillToVO(rs, fields, obj);
		return obj;
	}
	
	protected void fillToVO(ResultSet rs, Map fields, Object obj)throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) { // columns --> fields
			String name = rsmd.getColumnName(i);
			Field field = (Field) fields.get(name);
			if (field != null && !Modifier.isFinal(field.getModifiers())) {
				try {
					field.setAccessible(true);
					if(field.getType() == int.class)
						field.setInt(obj, rs.getInt(name));
					else if (field.getType() == Integer.class)
						field.set(obj, new Integer(rs.getInt(name)));
					else if (field.getType() == long.class)
						field.setLong(obj, rs.getLong(name));
					else if (field.getType() == Long.class)
						field.set(obj, new Long(rs.getLong(name)));
					else if (field.getType() == float.class)
						field.setFloat(obj, rs.getFloat(name));
					else if (field.getType() == Float.class)
						field.set(obj, new Float(rs.getFloat(name)));
					else if (field.getType() == double.class)
						field.setDouble(obj, rs.getDouble(name));
					else if (field.getType() == java.util.Date.class)
						field.set(obj, rs.getTimestamp(name));
					else if (field.getType() == Calendar.class) {
						if (rs.getTimestamp(name) != null) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(rs.getTimestamp(name));
							field.set(obj, cal);
						}
					} else if (field.getType() == String.class) 
						field.set(obj, rs.getString(name));
					else
						field.set(obj, rs.getObject(name));
//					logger.info(rsmd.getColumnName(i)+ ":" + rsmd.getColumnTypeName(i)
//							+ " (" + rsmd.getColumnClassName(i) + ")' ,'"+field.getType()+"'.");
				} catch (Exception e) {
					logger.error("cannot setValue from '" + rsmd.getColumnName(i)+ ":" + rs.getObject(name)
									+ " (" + rsmd.getColumnClassName(i) + ")' to '"+field.getType()+"'.");
//					e.printStackTrace();
				}
			}
		}
	}
	
	protected Map mappingColumnToFields(ResultSet rs, Class clazz, boolean isUnderscore) {
		Map table = new RowMap();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			Set fields = new HashSet();
			ReflectUtil.fetchAllFields(clazz, fields);
			fi:
			for (int i = 1; i <= rsmd.getColumnCount(); i++) { // columns --> fields
				String name = rsmd.getColumnName(i);
				String fName = null;
				if (isUnderscore)
					fName = StringUtil.convertUnderscoreToCamel(name);
				else
					fName = name;
				Iterator it = fields.iterator();
				while(it.hasNext()){
					Field field = (Field)it.next();
					if (fName.equals(field.getName())) {
						table.put(name, field);
						continue fi;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return table;
	}

	protected Object timestamp(Object obj) {
		if (obj instanceof Timestamp)
			return obj;
		else if (obj instanceof Calendar)
			return new Timestamp(((Calendar) obj).getTimeInMillis());
		else if (obj instanceof java.util.Date)
			return new Timestamp(((java.util.Date) obj).getTime());
		else
			return obj;
	}
	protected static String toString(Object[] values) {
        if (values == null)
            return "NULL";
        if (values.length == 0)
            return "[]";
 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            if (i == 0)
                buf.append('[');
            else
                buf.append(", ");
 
            buf.append(String.valueOf(values[i]));
        }
 
        buf.append("]");
        return buf.toString();
//		if(values==null)
//			return "";
//		int max = values.length - 1;
//		if(max<0)
//			return "";
//		StringBuffer buf = new StringBuffer("{");
//		if (values != null && values.length > 0) {
//			for (int i = 1; i <= max; i++) {
//				buf.append(values[i - 1]);
//				if (i < max)
//					buf.append(", ");
//			}
//			buf.append("}");
//		}
//		return buf.toString();
	}
	public static int countParams(String sql) {
		int i = 0, c = 0;
		while ((i = sql.indexOf("?")) != -1) {
			sql = sql.substring(i + 1);
			c++;
		}
		return c;
	}
	protected class QueryList extends LinkedList {
		private static final long serialVersionUID = 2418396343958184269L;
		private int gsize = 0;
		private int gid = 0;
		public QueryList(){
			super();
		}
		public QueryList(int group, String sql, Object[] values) {
			if (gid < group)
				gsize++;
			this.gid = group;
			add(group, sql, values);
		}
		public void add(int group, String sql, Object[] values) {
			if (gid < group)
				gsize++;
			this.gid = group;
			add(new Query(group, sql, values));
		}
		public int gsize() {
			return gsize;
		}
	}
	protected class Query {
		private int group;
		private String sql;
		private Object[] values;
		public Query(){
		}
		public Query(int group, String sql, Object[] values) {
			this.group = group;
			this.sql = sql;
			this.values = values;
		}
		public int getGroup() {
			return group;
		}
		public void setGroup(int group) {
			this.group = group;
		}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}
	}

}
