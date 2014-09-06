package to._2v.tools.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DebugTool {
	private static final Log logger = LogFactory.getLog(DebugTool.class);
	
	public static String toBeanString(Object obj) {
		return ReflectUtil.toString(obj);
	}
	
	public static String toBeanStringln(Object obj) {
		return ReflectUtil.toStringln(obj);
	}
	
	public static void printBeanString(Object obj,String text) {
		if (obj == null)
			return;
		if(logger.isDebugEnabled())
			logger.debug(text+" ========== >> >> >> "+obj.getClass().getName() + ":" + ReflectUtil.toString(obj));
	}
	
	public static void printBeanString(Object obj) {
		if (obj == null)
			return;
		if(logger.isDebugEnabled())
			logger.debug("========== >> >> >> "+obj.getClass().getName() + ":" + ReflectUtil.toString(obj));
	}

	public static void printlnBeanString(Object obj) {
		if (obj == null)
			return;
		if(logger.isDebugEnabled())
			logger.debug("=========== >> >> >> "+obj.getClass().getName() + ":" + ReflectUtil.toStringln(obj));
	}
}
