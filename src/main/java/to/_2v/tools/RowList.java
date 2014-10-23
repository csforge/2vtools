package to._2v.tools;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import to._2v.tools.util.StringUtil;


public class RowList<T> extends LinkedList<Object> {

	private static final long serialVersionUID = -7878472437260034984L;
	public Double getDouble(int index){
		return (Double)get(index);
	}
	public Integer getInt(int index){
		return (Integer)get(index);
	}
	public String getString(int index){
		return StringUtil.valueOf(get(index));
	}
	public Date getDate(int index){
		return (Date)get(index);
	}
	public Boolean getBoolean(int index){
		return (Boolean)get(index);
	}
	public Object getObject(int index){
		return get(index);
	}
	public List<?> getList(int index){
		return (List<?>)get(index);
	}
	public RowList getRowList(int index){
		return (RowList)get(index);
	}
	public RowMap getRowMap(int index){
		return (RowMap)get(index);
	}
}
