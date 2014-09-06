package to._2v.tools.dao;

import java.util.List;

import to._2v.tools.ValueObject;


public abstract class BaseDAO extends AbstractDAO {
	
	private String sql;
	private Object[] params;

	public BaseDAO() {
		super();
	}

	public BaseDAO(String ds) {
		super(ds);
	}

	public BaseDAO(boolean checked) {
		super(checked);
	}

	public BaseDAO(String ds, boolean checked) {
		super(ds, checked);
	}

	public int delete(ValueObject vo) {
		return this.delete(sql, vo);
	}

	public void find(ValueObject vo) {
		this.find(sql, vo);
	}

	public List search(ValueObject vo) {
		return this.searchEquals(sql, vo);
	}

	public int update(ValueObject vo) {
		return this.exeUpdate(sql, params);
	}
	
	public int create(ValueObject vo) {
		return this.exeUpdate(sql, params);
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}
	
	protected void rollback() {
		// TODO Auto-generated method stub
	}
}
