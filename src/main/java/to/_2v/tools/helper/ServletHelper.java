package to._2v.tools.helper;

import java.lang.reflect.Field;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.util.ReflectUtil;
import to._2v.tools.util.StringUtil;

public class ServletHelper {
	private static Log logger = LogFactory.getLog(ServletHelper.class);
	protected static String encoding = "utf-8";
	
	public void copyProperties0(Object obj, HttpServletRequest request){
		Iterator it = ReflectUtil.fetchAllPoolFields(obj);
		while(it.hasNext()){
			Field field = (Field)it.next();
			try {
				if (field.getType().isArray())
					ReflectUtil.setArrayValue(obj, field,  this.getParameters(request, field.getName()));
				else
					ReflectUtil.setValue(obj, field, this.getParameter(request, field.getName()));
			} catch (Exception e) {
				logger.warn("the resource: " + field, e);
			} 
		}
	}
	
	public String getParameter(HttpServletRequest request, String key) {
		String value = request.getParameter(key);
		if (value == null)
			return null;
		else
			value = value.trim();
		if (encoding != null && request.getMethod().equals("GET"))
			return StringUtil.toEncodingString(value, encoding);
		return value;
	}
	
	public String[] getParameters(HttpServletRequest request, String key) {
		return request.getParameterValues(key);
	}

}
