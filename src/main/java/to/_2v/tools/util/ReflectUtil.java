package to._2v.tools.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReflectUtil {
	private static final Log logger = LogFactory.getLog(ReflectUtil.class);
	private static Hashtable fieldMapPool = new Hashtable();
	private static Hashtable methodMapPool = new Hashtable();
	private static Hashtable fieldPool = new Hashtable();
	private static Hashtable methodPool = new Hashtable();
	private static synchronized void putFields(String key , HashSet value){
		fieldPool.put(key, value);
		logDebug("fieldPool: ", fieldPool);
	}
	private static synchronized void putMethods(String key , Method[] values){
		methodPool.put(key, values);
		logDebug("methodPool: ", methodPool);
	}
	public static void fetchAllFields(Class clazz, Map table) {
		Class superClazz = clazz.getSuperclass();
		if (superClazz != null)
			fetchAllFields(superClazz, table);

		Field[] fields = clazz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			if (!Modifier.isStatic(fields[j].getModifiers()))
				table.put(fields[j].getName(),fields[j]);
		}
	}
	public static void fetchAllFields(Class clazz, Set table) {
		Class superClazz = clazz.getSuperclass();
		if (superClazz != null)
			fetchAllFields(superClazz, table);

		Field[] fields = clazz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			if (!Modifier.isStatic(fields[j].getModifiers()))
				table.add(fields[j]);
		}
	}

	public static Iterator fetchAllFields(Object obj) {
		HashSet table = new HashSet();
		fetchAllFields(obj.getClass(), table);
		return table.iterator();
	}
	
	public static Iterator fetchPoolFields(Object obj) {
		String key = obj.getClass().getName();
		if (fieldPool.containsKey(key)) {
			return ((HashSet) fieldPool.get(key)).iterator();
		}
		HashSet table = new HashSet();
		fetchAllFields(obj.getClass(), table);
		putFields(key, table);
		return table.iterator();
	}
	
	public static void fetchAllNotNullFields(Object obj, Map table) {
		Set set = new HashSet();
		fetchAllFields(obj.getClass(), set);
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Field field = (Field) it.next();
			if (!field.getType().isPrimitive()
					&& !Modifier.isFinal(field.getModifiers())) {
				try {
					field.setAccessible(true);
					Object value = field.get(obj);
					if (value != null)
						table.put(field.getName(), value);
				} catch (Exception e) {
					logger.warn("cannot getValue from '" + field + "' in " + obj);
				}
			}
		}
	}
	
	public static Field getField(Object obj,String name){
		try {
			return obj.getClass().getField(name);
		} catch (SecurityException e) {
			logger.warn("connot find Field:" + e.getMessage());
		} catch (NoSuchFieldException e) {
			logger.warn("connot find Field:" + e.getMessage());
		}
		return null;
	}
	
	public static void set(Object obj, Field field, Object value) {
		if (value != null && field != null
				&& !Modifier.isStatic(field.getModifiers())
				&& !Modifier.isFinal(field.getModifiers())) {
			try {
				field.setAccessible(true);
				field.set(obj, value);
			} catch (Exception e) {
				logger.warn("cannot setValue to '" + field + "' from '" + value + "'.");
			}
		}
	}
	public static Method getMethod(Object baseObj, String name, Class paramType){
		if (StringUtil.isNotBlank(name)) {
			try {
				return baseObj.getClass().getMethod(name, new Class[] { paramType });
			} catch (Exception e) {
				logger.warn("cannot get method by '" + name + "'.");
			} 
		}
		return null;
	}
	public static void fetchMethods(Class clazz, Map table) {
		Method[] methods = clazz.getMethods();
		if (methods != null)
			for (int i = 0; i < methods.length; i++) {
				table.put(methods[i].getName(), methods[i]);
			}
	}
	public static Map getMethods(Object obj) {
		Hashtable table = new Hashtable();
		fetchMethods(obj.getClass(), table);
		return table;
	}
	public static Method[] fetchPoolMethods(Object obj) {
		String key = obj.getClass().getName();
		if (methodPool.containsKey(key)) {
			return ((Method[]) methodPool.get(key));
		}
		Method[] methods = obj.getClass().getMethods();
		putMethods(key, methods);
		return methods;
	}
	public static void invoke(Object obj, String methodName, Class[] paraTypes,
			Object[] paraValues) {
		Method method = null;
		try {
			method = obj.getClass().getMethod(methodName, paraTypes);
			method.invoke(obj, paraValues);
		} catch (Exception e) {
			logger.error("connot invoke method:" + method);
		}
	}
	
	public static void invoke(Object obj, String method) {
		try {
			obj.getClass().getMethod(method, null).invoke(obj, null);
		} catch (Exception e) {
			logger.error("connot invoke method:" + method);
		}
	}
	public static void setArrayValue(Object obj, Field field, String[] value) {
		if (value != null && field != null
				&& !Modifier.isFinal(field.getModifiers())) {
			try {
				field.setAccessible(true);
				field.set(obj, value);
			} catch (Exception e) {
				logger.warn("cannot setValue to '" + field + "' from '" + value + "'.");
			}
		}
	}
	public static void setObjectValue(Object obj, Field field, Object value) {
		if (value != null && !"".equals(value) && field != null
				&& !Modifier.isFinal(field.getModifiers())) {
			try {
				field.setAccessible(true);
				field.set(obj, value);
			} catch (Exception e) {
				logger.warn("cannot setValue to '" + field + "' from '" + value + "'.");
			}
		}
	}
	public static void setValue(Object obj, Method method, String value) {
		if (value != null && !"".equals(value) && method != null) {
			try {
				if (method.getParameterTypes() != null && method.getParameterTypes().length == 1){
					Class type = method.getParameterTypes()[0];
					if(type == String.class)
						method.invoke(obj, new String[]{value});

					else if (type == Integer.class || type == int.class)
						method.invoke(obj, new Object[]{new Integer(value)});
					
					else if (type == Float.class || type == float.class)
						method.invoke(obj, new Object[]{new Float(value)});
					
					else if (type == Long.class || type == long.class)
						method.invoke(obj, new Object[]{new Long(value)});
					
					else if (type == Double.class || type == double.class)
						method.invoke(obj, new Object[]{new Double(value)});
					
					else if (type == Boolean.class || type == boolean.class) {
						if ("1".equals(value) || "true".equals(value)
								|| "on".equals(value) || "yes".equals(value))
							method.invoke(obj, new Object[]{Boolean.TRUE});
						else
							method.invoke(obj, new Object[]{Boolean.FALSE});
//						 field.set(obj, new Boolean(value));
					} 
					
					else if (type == Short.class || type == short.class)
						method.invoke(obj, new Object[]{new Short(value)});
					
					else if (type == Date.class) {
						Date date = convertToDate(value);
						if (date != null)
							method.invoke(obj, new Object[]{date});
					}else if (type == Calendar.class) {
						Date date = convertToDate(value);
						if(date!=null){
							Calendar cal = Calendar.getInstance();
							cal.setTime(date);
							method.invoke(obj, new Object[]{cal});
						}
					}
				}

				
			} catch (Exception e) {
				logger.warn("cannot setValue to '" + method + "' with '" + value + "'.");
			}
		}
	}
	public static void setValue(Object obj, Field field, String value) {
		if (value != null && !"".equals(value) && field != null
				&& !Modifier.isFinal(field.getModifiers())) {
			try {
				field.setAccessible(true);
				if(field.getType() == String.class)
					field.set(obj, value);
				
				else if (field.getType() == int.class)
					field.setInt(obj, Integer.parseInt(value));
				else if (field.getType() == Integer.class)
					field.set(obj, new Integer(value));
				
				else if (field.getType() == float.class)
					field.setFloat(obj, Float.parseFloat(value));
				else if (field.getType() == Float.class)
					field.set(obj, new Float(value));
				
				else if (field.getType() == long.class)
					field.setLong(obj, Long.parseLong(value));
				else if (field.getType() == Long.class)
					field.set(obj, new Long(value));
				
				else if (field.getType() == double.class)
					field.setDouble(obj, Double.parseDouble(value));
				else if (field.getType() == Double.class)
					field.set(obj, new Double(value));
				
				else if (field.getType() == boolean.class){
					if ("1".equals(value) || "true".equals(value)
							|| "on".equals(value) || "yes".equals(value))
						field.setBoolean(obj, true);
					else
						field.setBoolean(obj, false);
//					field.setBoolean(obj, Boolean.getBoolean(value));
				} else if (field.getType() == Boolean.class) {
					if ("1".equals(value) || "true".equals(value)
							|| "on".equals(value) || "yes".equals(value))
						field.set(obj, Boolean.TRUE);
					else
						field.set(obj, Boolean.FALSE);
//					 field.set(obj, new Boolean(value));
				} 
				
				else if (field.getType() == short.class)
					field.setShort(obj, Short.parseShort(value));
				else if (field.getType() == Short.class)
					field.set(obj, new Short(value));
				
				else if (field.getType() == Date.class) {
					Date date = convertToDate(value);
					if (date != null)
						field.set(obj, date);
				}else if (field.getType() == Calendar.class) {
					Date date = convertToDate(value);
					if(date!=null){
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						field.set(obj, cal);
					}
				}
				
//				else if (field.getType() == DateTimeBean.class)
//					field.set(obj,new DateTimeBean(value));
//				else if (field.getType() == DateBean.class)
//					field.set(obj,new DateBean(value));
//				else if (field.getType() == TimeBean.class)
//					field.set(obj,new TimeBean(value));
				
			} catch (Exception e) {
				logger.warn("cannot setValue to '" + field + "' with '" + value + "'.");
			}
		}
	}

	static Date convertToDate(String value) {
		if (value.length() == 19)// "yyyy-MM-dd HH:mm:ss"
			return CalendarUtil.parseDateTime(value);
		else if (value.length() == 16)// "yyyy-MM-dd HH:mm"
			return CalendarUtil.parseDateTime2(value);
		else if (value.length() == 11)// "MM-dd HH:mm"
			return CalendarUtil.parseDateTimeMM(value);
		else if (value.length() == 10)// "yyyy-MM-dd"
			return CalendarUtil.parseDate(value);
		else if (value.length() == 8)// "yyyyMMdd"
			return CalendarUtil.parseDate(value, false);
		else if (value.length() == 6)// "yyMMdd"
			return CalendarUtil.parseDateDaySimpleMiddle(value);
		else if (value.length() == 5)// "HH:mm"
			return CalendarUtil.parseTimeMinute(value);
		else if (value.length() == 4)// "MMdd"
			return CalendarUtil.parseDaySimpleShort(value);
		else
			return null;
	}
	public static void setter(Object baseObj, String name, Object value) {
		Field field = getPoolField(baseObj,name);
		if (field != null)
			setter(baseObj, name, field.getType(), value);
	}
	public static void setter(Object baseObj, String fieldName, Class clazz, Object value) {
//		Field field = baseObj.getClass().getField(fieldName);
//		field.setAccessible(true);
//		field.set(baseObj, value);
		if (value != null && fieldName != null && !"".equals(fieldName)) {
			try {
				Method method = baseObj.getClass().getMethod(
						StringUtil.getSetMethodName(fieldName), new Class[] { clazz });
				if (method != null)
					method.invoke(baseObj, new Object[] { value });
			} catch (Exception e) {
				logger.warn("cannot invoke setter to '" + fieldName + "' from '" + value + "'.");
			} 
		}
	}
	public static Object getter(Object baseObj, String fieldName) {
//		Field field = baseObj.getClass().getField(fieldName);
//		field.setAccessible(true);
//		 return field.get(baseObj);
		if (fieldName != null && !"".equals(fieldName)) {
			try {
				Method method = baseObj.getClass().getMethod(
						StringUtil.getGetMethodName(fieldName), null);
				if (method != null)
					return method.invoke(baseObj, null);
			} catch (Exception e) {
				logger.warn("cannot invoke getter from '" + fieldName + "'.");
				return null;
			}
		}
		return null;
	}

	public static void copyProperties(Object dest, Object orig) {
		copyProperties(dest.getClass(), dest, orig);
	}
	private static void copyProperties(Class clazz, Object dest, Object orig){
		Class superClazz = clazz.getSuperclass();
		if (superClazz != null)
			copyProperties(superClazz, dest, orig);

		Field[] fields = clazz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			if (!Modifier.isStatic(fields[j].getModifiers())
					&& !Modifier.isFinal(fields[j].getModifiers()))
				try {
					fields[j].setAccessible(true);
					Object value = fields[j].get(orig);
//					System.out.println(fields[j]+": "+value);
					if (value != null){
//						if(fields[j].getType().isPrimitive())
//							fields[j].set(dest, value);
//						else
							fields[j].set(dest, value);
					}
				} catch (Exception e) {
					logger.warn("cannot copy property ." + e.getMessage());
				} 
		}
	}
	public static String toStringln(Object obj) {
		return toString(obj, true);
	}
	public static String toString(Object obj) {
		return toString(obj, false);
	}
	public static String toString(Object obj, boolean newline) {
		if (obj == null)
			return "null";
		StringBuffer buf = new StringBuffer("{");		
		HashSet table = new HashSet();
		fetchAllFields(obj.getClass(), table);
		Iterator it = table.iterator();
		int c = 0;
		while (it.hasNext()) {
			Field field = (Field) it.next();
			try {
				if (!Modifier.isStatic(field.getModifiers())
						&& !Modifier.isFinal(field.getModifiers())) {
					field.setAccessible(true);
					Object value = field.get(obj);
					if (value != null) {
						if (c > 0)
							if (newline)
								buf.append("\n");
							else
								buf.append(", ");
						if (value instanceof Calendar)
							value = ((Calendar) value).getTime();
						buf.append(field.getName()).append("=").append(value);
						c++;
					}
				}
			} catch (Exception e) {
				// logger.warn("the resource: " + field, e);
			}
		}
		buf.append("}");
		return buf.toString();
	}
	
	private static synchronized void putFields(String key , Map value){
		fieldMapPool.put(key, value);
		logDebug("fieldMapPool: ", fieldMapPool);
	}
	public static Map lookupFieldPool(String key){
		return (Map)fieldMapPool.get(key);
	}
	public static Field lookupField(String key, String name) {
		Map map = lookupFieldPool(key);
		return (Field) map.get(name);
	}
	private static boolean containsFieldsKey(String key){
		return fieldMapPool.containsKey(key);
	}
	public static Field getPoolField(Object baseObj, String name) {
		Map map = getPoolFields(baseObj);
		if (map != null)
			return (Field) map.get(name);
		return null;
	}
	public static Map getPoolFields(Object baseObj) {
		String key = baseObj.getClass().getName();
		if (!containsFieldsKey(key)) {
			Hashtable table = new Hashtable();
			fetchAllFields(baseObj.getClass(), table);
			putFields(key, table);
			return table;
		} else {
			return lookupFieldPool(key);
		}
	}
	public static Iterator fetchAllPoolFields(Object obj) {
		HashSet table = new HashSet();
		fetchAllFields(obj.getClass(), table);
		return table.iterator();
	}
	private static synchronized void putMethods(String key , Map value){
		methodMapPool.put(key, value);
		logDebug("methodMapPool: ", methodMapPool);
	}
	public static Map lookupMethodPool(String key){
		return (Map)methodMapPool.get(key);
	}
	public static Method lookupMethod(String key, String name) {
		Map map = lookupMethodPool(key);
		return (Method) map.get(name);
	}
	private static boolean containsMethodsKey(String key){
		return methodMapPool.containsKey(key);
	}
	public static Method getPoolMethod(Object baseObj, String name) {
		Map map = getPoolMethods(baseObj);
		if (map != null)
			return (Method) map.get(name);
		return null;
	}
	public static Map getPoolMethods(Object baseObj) {
		String key = baseObj.getClass().getName();
		if (!containsMethodsKey(key)) {
			Hashtable table = new Hashtable();
			fetchMethods(baseObj.getClass(), table);
			putMethods(key, table);
			return table;
		} else {
			return lookupMethodPool(key);
		}
	}
	private static void logDebug(String poolName, Hashtable pool){
		if(logger.isDebugEnabled()){
			StringBuffer buf = new StringBuffer(poolName+": ");
			Iterator it = pool.keySet().iterator();
			while(it.hasNext()){
				String pk = (String)it.next();
				buf.append(pk).append(", ").append(((Map)pool.get(pk)).size()).append("; ");
			}
			logger.debug(buf.toString());
		}
	}
}
