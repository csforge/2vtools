package to._2v.tools;

import java.util.Date;
import java.util.HashMap;

import to._2v.tools.util.StringUtil;


public class RowMap extends HashMap<Object, Object>{

	private static final long serialVersionUID = -6430448678167640917L;
	public Double getDouble(Object key){
		return (Double)get(key);
	}
	public Integer getInt(Object key){
		return (Integer)get(key);
	}
	public String getString(Object key){
		return StringUtil.valueOf(get(key));
	}
	public Date getDate(Object key){
		return (Date)get(key);
	}
	public Boolean getBoolean(Object key){
		return (Boolean)get(key);
	}
	public Object getObject(Object key){
		return get(key);
	}
	public RowList getRowList(Object key){
		return (RowList)get(key);
	}
	public RowMap getRowMap(Object key){
		return (RowMap)get(key);
	}
}
