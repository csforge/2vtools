package to._2v.tools.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.RowMap;
import to._2v.tools.ValueObject;
import to._2v.tools.util.CalendarUtil;
import to._2v.tools.util.ReflectUtil;


/**
 * 
 * @author jerry2
 * 
 */
public abstract class AbstractDAO extends DAOUtil {

	private static final Log logger = LogFactory.getLog(AbstractDAO.class);
	PreparedStatement psmt;
	Connection conn;

	public AbstractDAO() {
		super();
	}
	public AbstractDAO(boolean checked) {
		super(checked);
	}
	public AbstractDAO(String ds) {
		super(ds);
	}
	public AbstractDAO(String ds, boolean checked) {
		super(ds, checked);
	}
	public void close() {
		DAOUtil.close(psmt);
		DAOUtil.close(conn);
	}

	public void cleanup() {
		close();
		psmt = null;
		conn = null;
	}
	
	public static String getSQLString(String key) {
		return Persist.getSQLString(key);
	}

	protected void setPreparedStatement(PreparedStatement psmt) {
		this.psmt = psmt;
	}

	public void setPreparedStatement(String sql) {
		if(logger.isInfoEnabled() && logger.isInfoEnabled())
			logger.info(sql);
		try {
			conn = getConnection();
			psmt = conn.prepareStatement(sql);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public void setPreparedStatementValues(Object[] values)
			throws SQLException {
		setPreparedStatementValues(psmt, values);
	}
	public void setPreparedStatementValues(String sql, List values)
			throws SQLException {
		setPreparedStatement(sql);
		setPreparedStatementValues(psmt, values);
	}
	public void setPreparedStatementValues(String sql, Object[] values)
			throws SQLException {
		setPreparedStatement(sql);
		setPreparedStatementValues(psmt, values);
	}
	public void setPatternMatchingPreparedStatementValues(String sql, String column, Object value)
			throws SQLException {
		if (column == null || column.equals("") || value == null
				|| value.equals("")) {
			setPreparedStatement(sql);
		} else {
			setPreparedStatementValues(sql + " where " + column + " like ?",
					new String[] { "%" + value + "%" });
		}
	}
	public void setPatternMatchingPreparedStatementValues(String sql,
			String[] columns, Object[] values) throws SQLException {
		if (columns == null || values == null || columns.length < 1
				|| columns.length != values.length) {
			setPreparedStatement(sql);
		} else {
			StringBuffer sb = new StringBuffer(sql);
			List valuesPars = new LinkedList();
			for (int i = 0, c = 0; i < columns.length; i++) {
				if (columns[i].equals(""))
					continue;
				if (c++ <= 0)
					sb.append(" where ");
				else
					sb.append(" and ");
				sb.append(columns[i]).append(" like ?");
				valuesPars.add("%" + values[i] + "%");
			}
			setPreparedStatementValues(sb.toString(), valuesPars);
		}
	}
	public ResultSet getRs(String sqlstr) throws SQLException {
		return super.getRs(sqlstr);
	}
	
	public List exeSelect(Object[] values) {
		return exeSelect(psmt, values);
	}

	public List exeRecordsSelect(Object[] values) {
		return exeRecordsSelect(psmt, values);
	}

	public List exeFieldsSelect(Object[] values) {
		return exeFieldsSelect(psmt, values);
	}

	public Object getSelectValue(Object[] values) {
		return getSelectValue(psmt, values);
	}

	public int exeUpdate(Object[] values) {
		return exeUpdate(psmt, values);
	}
	
	public int exeUpdate(List values) {
		return exeUpdate(psmt, values);
	}

	public Map fillClumnData(Object[] values) {
		return fillClumnData(psmt, values);
	}

	public List fillRowsData(Object[] values) {
		return fillRowsData(psmt, values);
	}

	public String getStringValue(Object[] values) {
		return String.valueOf(getSelectValue(values));
	}

	public int getIntValue(Object[] values) {
		int value = -1;
		try {
			value = Integer.parseInt(getStringValue(values));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return value;
	}

	public double getDoubleValue(Object[] values) {
		double value = -1;
		try {
			value = Double.parseDouble(getStringValue(values));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return value;
	}

	public Calendar getCalendarValue(Object[] values) {
		Calendar theDate = Calendar.getInstance();
		try {
			theDate.setTime(getDateValue(values));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return theDate;
	}

	public Date getDateValue(Object[] values) {
		Date theDate = null;
		try {
			theDate = (Timestamp) getSelectValue(values);
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return theDate;
	}

	public String getDateString(Object[] values) {
		String str = null;
		try {
			str = CalendarUtil.toDateString(getDateValue(values).getTime());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return str;
	}

	/**
	 * 
	 * @param sql
	 * @param vo
	 *            with "ids[]"
	 * @return
	 */
	public int delete(String sql, ValueObject vo) {
		return delete(sql, vo.getIds());
	}
	public Map fill(String sql, Object[] values, ValueObjectCommand command) {
		Map table = new RowMap();
		fill(sql, values, command, table);
		return table;
	}
	public void fill(String sql, Object[] values, ValueObjectCommand command, Map table) {
		ResultSet rs = null;
		try {
			setPreparedStatementValues(sql,values);
			
			rs = psmt.executeQuery();
			while(rs.next()){
//				table.put(rs.getObject(1), rs.getObject(2));
				command.execute(rs, table);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.warn(toString(values));
		}finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
	}
	/**
	 * 
	 * @param sql
	 * @param columns
	 * @param values
	 * @param methodName the method must be "methodName(ResultSet rs, ValueObject vo)"
	 * @return
	 */
	public List search(String sql, String[] columns, Object[] values, Object baseObj,
			String methodName) {
		ResultSet rs = null;
		List results = null;
		try {
			// 1.prepare a statement
			setPatternMatchingPreparedStatementValues(sql, columns, values);

			// 2.get the results
			results = new LinkedList();
			rs = psmt.executeQuery();
			Method method = baseObj.getClass().getMethod(methodName,
					new Class[] { ResultSet.class, ValueObject.class });
			while (rs.next()) {
				try {
					ValueObject vo = (ValueObject) baseObj.getClass().newInstance();
					method.invoke(baseObj, new Object[] { rs, vo });
					results.add(vo);
				} catch (Exception e) {
					logger.error(e.getMessage());
				} 
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		return results;
	}
	
	public List search(String sql, String[] columns, Object[] values, ValueObjectCommand command) {
		ResultSet rs = null;
		List results = null;
		try {
			// 1.prepare a statement
			setPatternMatchingPreparedStatementValues(sql, columns, values);

			// 2.get the results
			results = new LinkedList();
			rs = psmt.executeQuery();
			while (rs.next()) {
				Object vo = command.execute(rs);
				results.add(vo);
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		return results;
	}
	/**
	 * 
	 * @param sql
	 * @param values
	 * @param methodName the method must be "methodName(ResultSet rs, ValueObject vo)"
	 * @return
	 */
	public List search(String sql, Object[] values, Object baseObj, String methodName) {
		ResultSet rs = null;
		List results = null;
		try {
			// 1.prepare a statement
				setPreparedStatementValues(sql, values);
	
			// 2.get the results
			results = new LinkedList();
			rs = psmt.executeQuery();
			Method method = baseObj.getClass().getMethod(methodName,
					new Class[] { ResultSet.class, ValueObject.class });
			while (rs.next()) {
				ValueObject vo = null;
				try {
					vo = (ValueObject) baseObj.getClass().newInstance();
					method.invoke(baseObj, new Object[] { rs, vo });
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				results.add(vo);
			}

		} catch (Exception e1) {
			logger.error(e1.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		
		return results;
	}
	
	public List search(String sql, Object[] values, ValueObjectCommand command) {
		ResultSet rs = null;
		List results = null;
		try {
			// 1.prepare a statement
			setPreparedStatementValues(sql, values);
			
			// 2.get the results
			results = new LinkedList();
			rs = psmt.executeQuery();
			while (rs.next()) {
				Object vo = command.execute(rs);
				results.add(vo);
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		return results;
	}
	/**
	 * 
	 * @param sql
	 * @param values
	 * @param methodName the method must be "methodName(ResultSet rs, ValueObject vo)"
	 * @return
	 */
	public ValueObject find(String sql, Object[] values, Object baseObj, String methodName) {
		ResultSet rs = null;
		ValueObject vo = null;
		try {
			// 1.prepare a statement
			setPreparedStatementValues(sql, values);

			// 2.get a result
			rs = psmt.executeQuery();
			Method method = baseObj.getClass().getMethod(methodName,
					new Class[] { ResultSet.class, ValueObject.class });
			while (rs.next()) {
				vo = (ValueObject) baseObj.getClass().newInstance();
				method.invoke(baseObj, new Object[] { rs, vo });
				break;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		return vo;
	}
	
	public Object find(String sql, Object[] values, ValueObjectCommand command) {
		ResultSet rs = null;
		Object vo = null;
		try {
			// 1.prepare a statement
			setPreparedStatementValues(sql, values);

			// 2.get a result
			rs = psmt.executeQuery();
			while (rs.next()) {
				vo = command.execute(rs);
				break;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} finally{
			// 3.clean resource.
			close(rs);
			cleanup();
		}
		return vo;
	}

	/**
	 * 
	 * @param sql
	 * @param vo
	 * @param methodName the method must be "methodName(ValueObject vo)" and returns "Object[]/List"
	 * @return
	 */
	public int updateFormVO(String sql, List values, Object baseObj,
			String methodName)  {
		int c = 0;
		try {
			// 1.prepare a statement
			setPreparedStatement(sql);

			// 2. execute update
			Method method = baseObj.getClass().getMethod(methodName,
					new Class[] { ValueObject.class });
			for (int i = 0; i < values.size(); i++) {
				try {
					Object methodArgs = method.invoke(baseObj, new Object[] { values.get(i) });
					if (methodArgs instanceof List)
						c += exeUpdate((List) methodArgs);
					else
						c += exeUpdate((Object[]) methodArgs);
				} catch (Exception e) {
					logger.error(e.getMessage());
				} 
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally{
			// 3.clean resource.
			cleanup();
		}
		return c;
	}
	public int updates(String sql, Object[] values, ValueObjectCommand command) {
		// 1.prepare a statement
		setPreparedStatement(sql);

		// 2. execute update
		int c = 0;
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				Object[] value = command.execute(values[i]);
				if (value != null)
					c += exeUpdate(value);
			}
		}
		
		// 3.clean resource.
		cleanup();
		return c;
	}
	
	public int updates(String sql, List values, ValueObjectCommand command) {
		// 1.prepare a statement
		setPreparedStatement(sql);

		// 2. execute update
		int c = 0;
		if (values != null) {
			Iterator it = values.iterator();
			while (it.hasNext()) {
				Object[] value = command.execute(it.next());
				if (value != null)
					c += exeUpdate(value);
			}
		}
		
		// 3.clean resource.
		cleanup();
		return c;
	}
	
	public List search(String sql, Object searchObj, boolean isMatching, boolean isPattern){
		// 1.arrange sql  //int len = StringUtils.countMatches(sql, "?");
		Map table = new HashMap();
		ReflectUtil.fetchAllNotNullFields(searchObj, table);
		
		StringBuffer sb = new StringBuffer(sql);
		Vector values = new Vector();
		Iterator it = table.entrySet().iterator();
		int c = 0;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (c > 0)
				sb.append("and ");
			else
				sb.append(" where ");// select * from table where id=?
			sb.append(entry.getKey());
			if(!isPattern){
				if(isMatching)
					sb.append("=? ");
				else
					sb.append("!=? ");
				values.add(entry.getValue());
			} else {
				if(isMatching)
					sb.append(" like ? ");
				else
					sb.append(" not like ? ");
				values.add("%"+entry.getValue()+"%");
			}
			c++;
		}
		
		// 2.get results
		return this.getResultList(sql, values.toArray(), searchObj.getClass());
	}
	
	public List searchEquals(String sql, Object searchObj) {
		return search(sql, searchObj, true, false);
	}

	public List searchLike(String sql, Object searchObj) {
		return search(sql, searchObj, true, true);
	}
	
	public void find(String sql,ValueObject vo){
		getResult(sql, new Object[]{vo.getId()}, vo);
	}
	
	/**
	 * 
	 * @param vo the vo.ids[] or id is the where-calus  conditions  
	 * @return either (1) the row count for <code>DELETE</code> statements
     *         or (2) 0 for SQL statements that return nothing
	 */
	public abstract int delete(ValueObject vo);
	/**
	 * 
	 * @param 
	 * @return either (1) the row count for <code>INSERT</code> statements
     *         or (2) 0 for SQL statements that return nothing
	 */
	public abstract int create(ValueObject vo);
	/**
	 * 
	 * @param vo
	 * @return either (1) the row count for <code>UPDATE</code> statements
     *         or (2) 0 for SQL statements that return nothing
	 */
	public abstract int update(ValueObject vo);
	/**
	 * search by PrimaryKey
	 * @param vo the vo.id specify PrimaryKey
	 */
	public abstract void find(ValueObject vo);
	public abstract List search(ValueObject vo);
	
}
