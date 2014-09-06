package to._2v.tools;

import java.io.Serializable;

public class ValueObject implements Serializable,Comparable<Object> {

	private static final long serialVersionUID = 4925939280031011227L;
	protected String id;
	protected String[] ids;
	protected Object obj;
	protected Object temp;
	protected String str;
	protected String flag;
	protected Object operateTime;
	protected String operator;
	private String sqlKey;
	private Object[] params;
	private Integer current;
	
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getStr() {
		if (str != null && str.length() < 1)
			return null;
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public int compareTo(Object object) {
		try {
			ValueObject temp = (ValueObject) object;
			if (obj.toString().compareTo(temp.obj.toString()) > 0) {
				return 1;
			} else if (obj.equals(temp.obj)){
				return 0;
			} else {
				return -1;
			}
		} catch (Exception ex) {
			return -1;
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String[] getIds() {
		return ids;
	}
	public void setIds(String[] ids) {
		this.ids = ids;
	}
	public Object getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Object operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSqlKey() {
		return sqlKey;
	}
	public void setSqlKey(String sqlKey) {
		this.sqlKey = sqlKey;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public Object getTemp() {
		return temp;
	}
	public void setTemp(Object temp) {
		this.temp = temp;
	}

}
