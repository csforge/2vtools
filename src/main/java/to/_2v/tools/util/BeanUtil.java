package to._2v.tools.util;

public class BeanUtil {

	
	public static String toString(Object obj){
		return ReflectUtil.toString(obj);
	}
	
	public static void copyProperties(Object dest, Object orig) {
		ReflectUtil.copyProperties(dest, orig);
	}
}
