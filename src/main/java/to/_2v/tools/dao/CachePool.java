package to._2v.tools.dao;

import java.util.Hashtable;
import java.util.List;

public class CachePool {

	private static Hashtable pool = new Hashtable();

	synchronized public static void put(String key, Object value) {
		pool.put(key, value);
	}

	public static Object get(String key) {
		return pool.get(key);
	}

	public static void clean(String key) {
		pool.remove(key);
	}
	
	public static void remove(String key) {
		pool.remove(key);
	}

	public static List get(String key, int fromIndex, int toIndex) {
		List results = (List) pool.get(key);
		if (results != null)
			return results.subList(fromIndex, toIndex);
		else
			return null;
	}

}
