package to._2v.tools.dao.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.ValueObject;
import to._2v.tools.dao.AbstractDAO;
import to._2v.tools.dao.Persist;
import to._2v.tools.dao.ValueObjectCommandAdapter;


public class DemoDAOImpl extends AbstractDAO implements DemoDAO {

	private static final Log logger = LogFactory.getLog(DemoDAOImpl.class);
	private static String select_sql = "select * from xxtable";
	
	public int create(ValueObject vo) {
		return this.exeUpdate(Persist.getSQLString(vo.getSqlKey()), vo.getParams());
	}

	public int delete(ValueObject vo) {	
		return this.delete(Persist.getSQLString(vo.getSqlKey()), vo.getIds());
	}

	public int update(ValueObject vo) {
		return this.exeUpdate(Persist.getSQLString(vo.getSqlKey()), vo.getParams());
	}
	
	public void find(ValueObject vo) {
		vo = (ValueObject) this.find(Persist.getSQLString(vo.getSqlKey()), vo.getParams(),
			new ValueObjectCommandAdapter() {
				public Object execute(ResultSet rs) {
					try {
						ValueObject bean = new ValueObject();
						bean.setId(rs.getString("id"));
						//...
						return bean;
					} catch (SQLException e) {
						logger.warn("ValueObjectCommand error: "+e);
						return null;
					}
				}
			});
	}

	public List search(ValueObject vo) {
		return this.search(Persist.getSQLString(vo.getSqlKey()), vo.getParams(),
			new ValueObjectCommandAdapter() {
				public Object execute(ResultSet rs) {
					try {
						ValueObject bean = new ValueObject();
						bean.setId(rs.getString("id"));
						//...
						return bean;
					} catch (SQLException e) {
						logger.warn("ValueObjectCommand error: "+e);
						return null;
					}
				}
			});
	}

	public List searchEquals(Object obj) {
		return this.searchEquals(select_sql, obj);
	}
	
	public List searchLike(Map table) {
		Vector values = new Vector();
		String sql = arrangeSQL(table, select_sql, values);
		return this.search(sql, values.toArray(),
				new ValueObjectCommandAdapter() {
			public Object execute(ResultSet rs) {
				try {
					ValueObject bean = new ValueObject();
					bean.setId(rs.getString("id"));
					//...
					return bean;
				} catch (SQLException e) {
					logger.warn("ValueObjectCommand error: "+e);
					return null;
				}
			}
		});
	}
	
	boolean isMatching = true;
	public String arrangeSQL(Map table, String sql, Vector values) {
		StringBuffer sb = new StringBuffer(sql);
		Iterator it = table.entrySet().iterator();
		int c = 0;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (c > 0)
				sb.append("and ");
			else
				sb.append(" where ");// select * from table where id=?
			sb.append(entry.getKey());
			if (isMatching)
				sb.append(" like ? ");
			else
				sb.append(" not like ? ");
			values.add("%" + entry.getValue() + "%");
			c++;
		}
		return sb.toString();
	}

	protected void rollback() {
	}
}
