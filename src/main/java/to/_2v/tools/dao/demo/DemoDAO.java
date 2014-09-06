package to._2v.tools.dao.demo;


import java.util.List;
import java.util.Map;


public interface DemoDAO {
	
	/**
	 * search by the Condition
	 * @param vo the condition in which fields is not null or ""
	 * @return List of results
	 */
	public List searchEquals(Object obj);
	
	/**
	 * search by the Condition
	 * @param map the Map element that the column which the key specified contains the value(equals/like)
	 * @return List of results
	 */
	public List searchLike(Map map);
	
	

}
