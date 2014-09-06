package to._2v.tools;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;

public class ObjectPool {
	private static Log logger = LogFactory.getLog(ObjectPool.class);
	private static Hashtable<String, Object> pool = new Hashtable<String, Object>();
	
	synchronized public static void put(String key,Object value){
		pool.put(key, value);
	}
	public static Object borrowObject(String key, Class<?> clazz) {
		Object obj = lookup(key);
		if (obj == null) {
			put(key, new StackObjectPool(new ObjectFactory(clazz)));
			obj = lookup(key);
		}
		try {
			return ((StackObjectPool)obj).borrowObject();
		} catch (Exception e) {
			logger.error("cannot borrow the object " + clazz, e);
		}
		return null;
	}
	public static Object borrowObject(String key, Class<?> clazz,
			Class<?>[] parameterTypes, Object[] initargs) {
		Object obj = lookup(key);
		if (obj == null) {
			put(key, new StackObjectPool(new ObjectFactory(clazz,
					parameterTypes, initargs)));
			obj = lookup(key);
		}
		try {
			return ((StackObjectPool)obj).borrowObject();
		} catch (Exception e) {
			logger.error("cannot borrow the object " + clazz, e);
		}
		return null;
	}
	public static void returnObject(String key, Object obj) {
		try {
			StackObjectPool poolObj = (StackObjectPool)lookup(key);
			poolObj.returnObject(obj);
		} catch (Exception e) {
			logger.error("cannot return the object " + obj, e);
		}
	}
	public static Object getInstance(String key, Class<?> clazz) {
//		return borrowObject(key, clazz);
		return getSingleton(key, clazz);
	}
	public static Object getInstance(String key, Class<?> clazz,
			Class<?>[] parameterTypes, Object[] initargs) {
//		return borrowObject(key, clazz, parameterTypes, initargs);
		return getSingleton(key, clazz, parameterTypes, initargs);
	}
	/**
	 * Get a singleton Object
	 * eg. (LoginService)ObjectPool.getInstance("login_service",LoginServiceImpl.class);
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static Object getSingleton(String key, Class<?> clazz) {
		Object obj = lookup(key);
		if (obj == null) {
			put(key, clazz);
			obj = lookup(key);
		}
		return obj;
	}
	/**
	 * Get a singleton Object
	 * eg. (LoginService)ObjectPool.getInstance("login_service",LoginServiceImpl.class,new Class[]{},new Object[]);
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static Object getSingleton(String key, Class<?> clazz,
			Class<?>[] parameterTypes, Object[] initargs) {
		Object obj = lookup(key);
		if (obj == null) {
			put(key, clazz, parameterTypes, initargs);
			obj = lookup(key);
		}
		return obj;
	}
	
	public static void put(String key, Class<?> clazz) {
		try {
			put(key, clazz.newInstance());
		} catch (Exception e) {
			logger.error("cannot instanite " + clazz, e);
		} 
	}
	public static void put(String key, Class<?> clazz, Class<?>[] parameterTypes, Object[] initargs){
		try {
			Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);
			put(key, constructor.newInstance(initargs));
		} catch (Exception e) {
			logger.error("cannot instanite " + clazz, e);
		} 
	}
	
	public static Object lookup(String key){
		return pool.get(key);
	}
	
	public static boolean duplicate(String key){
		if(pool.containsKey(key))
			return true;
		return false;
	}
	
}
class ObjectFactory extends BasePoolableObjectFactory {
	private Object obj = null;
	public ObjectFactory(Class<?> clazz){
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ObjectFactory(Class<?> clazz,Class<?>[] parameterTypes, Object[] initargs){
		Constructor<?> constructor = null;
		try {
			constructor = clazz.getDeclaredConstructor(parameterTypes);
			obj = constructor.newInstance(initargs);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public Object makeObject() throws Exception {
		return obj;
	}
	
    /** No-op. */
    public void passivateObject(Object obj) 
        throws Exception {
    	
    }
}
