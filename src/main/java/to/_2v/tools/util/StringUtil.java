package to._2v.tools.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {
	/**
	 * Returns echoEqual String while obj1.toString.equals(obj2),else echoNotEqual String
	 * @param obj1
	 * @param obj2
	 * @param echoEqual
	 * @param echoNotEqual
	 * @return 
	 */
	public static String ifElseString(Object obj1,Object obj2,String echoEqual,String echoNotEqual){
		return ifElseString(isEqual(obj1,obj2),echoEqual,echoNotEqual);
	}
	/**
	 * Returns str1 while boo is true,else str2
	 * @param boo if-else condition
	 * @param str1
	 * @param str2
	 * @return if the boo argument is true,then a string equal to str1;otherwise str2 is returned.
	 */
	public static String ifElseString(boolean boo,String str1,String str2){
		return boo ? str1 : str2;
	}
	/**
	 * Returns true while obj1.toString.equals(obj2)
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean isEqual(Object obj1,Object obj2){
		if(obj1==null || obj2==null)
			return false;
		if(valueOf(obj1).equals(valueOf(obj2)))
			return true;
		else
			return false;
	}
    /**
     * Returns the string representation of the <code>Object</code> argument.
     *
     * @param   obj   an <code>Object</code>.
     * @return  if the argument is <code>null</code>, then a string equal to
     *          <code>""</code>; otherwise, the value of
     *          <code>obj.toString()</code> is returned.
     * @see     java.lang.Object#toString()
     */
    public static String valueOf(Object obj) {
    	return (obj == null) ? "" : obj.toString();
    }
    /**
     * Returns the string representation of the <code>int</code> argument.
     * <p>
     * The representation is exactly the one returned by the
     * <code>Integer.toString</code> method of one argument.
     *
     * @param   i   an <code>int</code>.
     * @return  a string representation of the <code>int</code> argument.
     * @see     java.lang.Integer#toString(int, int)
     */
    public static String valueOf(int i) {
        return String.valueOf(i);
    }

    /**
     * Returns the string representation of the <code>long</code> argument.
     * <p>
     * The representation is exactly the one returned by the
     * <code>Long.toString</code> method of one argument.
     *
     * @param   l   a <code>long</code>.
     * @return  a string representation of the <code>long</code> argument.
     * @see     java.lang.Long#toString(long)
     */
    public static String valueOf(long l) {
        return String.valueOf(l);
    }

    /**
     * Returns the string representation of the <code>float</code> argument.
     * <p>
     * The representation is exactly the one returned by the
     * <code>Float.toString</code> method of one argument.
     *
     * @param   f   a <code>float</code>.
     * @return  a string representation of the <code>float</code> argument.
     * @see     java.lang.Float#toString(float)
     */
    public static String valueOf(float f) {
    	return String.valueOf(f);
    }

    /**
     * Returns the string representation of the <code>double</code> argument.
     * <p>
     * The representation is exactly the one returned by the
     * <code>Double.toString</code> method of one argument.
     *
     * @param   d   a <code>double</code>.
     * @return  a  string representation of the <code>double</code> argument.
     * @see     java.lang.Double#toString(double)
     */
    public static String valueOf(double d) {
    	return String.valueOf((int)d);
    }
    /**
     * Returns the string representation of the <code>boolean</code> argument.
     *
     * @param   b   a <code>boolean</code>.
     * @return  if the argument is <code>true</code>, a string equal to
     *          <code>"true"</code> is returned; otherwise, a string equal to
     *          <code>"false"</code> is returned.
     */
    public static String valueOf(boolean b) {
    	return  String.valueOf(b);
    }

    /**
     * Returns the string representation of the <code>char</code>
     * argument.
     *
     * @param   c   a <code>char</code>.
     * @return  a string of length <code>1</code> containing
     *          as its single character the argument <code>c</code>.
     */
    public static String valueOf(char c) {
    	return String.valueOf(c);
    }
    /**
     * Returns the string representation of the <code>Double</code>
     * argument without ".0"
     * 
     * @param d
     * @return
     */
    public static String valueOf(Double d){
    	return valueOf(NumberUtil.getInt(d));
    }
    
    public static String concat(String[] strs){
    	if(strs==null || strs.length==0)
    		return null;
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<strs.length;i++)
    		sb.append(strs[i]);
    	return sb.toString();
    }
    public static String concat(String str,String split){
    	String[] strs = str.split(split);
    	if(strs==null || strs.length==0)
    		return null;
    	StringBuffer sb = new StringBuffer();
    	for(int i=0;i<strs.length;i++)
    		sb.append(strs[i]);
    	return sb.toString();
    }
    public static String getGetMethodName(String field) {
		return new StringBuffer().append("get").append(
				Character.toUpperCase(field.charAt(0))).append(
				field.substring(1)).toString();
	}
    public static String getSetMethodName(String field) {
		return new StringBuffer().append("set").append(
				Character.toUpperCase(field.charAt(0))).append(
				field.substring(1)).toString();
	}
    public static String convertUnderscoreToCamel(String underscore) {
		char[] cs = underscore.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			if ('_' != cs[i])
				sb.append(cs[i]);
			else
				sb.append(Character.toUpperCase(cs[++i]));
		}
		return sb.toString();
	}
	public static String convertCamelToUnderscore(String camel) {
		char[] cs = camel.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cs.length; i++) {
			if (!Character.isUpperCase(cs[i]))
				sb.append(cs[i]);
			else
				sb.append('_').append(Character.toLowerCase(cs[i]));
		}
		return sb.toString();
	}
	   /**
     * <p>Checks if a String is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    public static boolean isNotBlank(String str) {
    	return !isBlank(str);
    }
    public static String valueOf(int len,Object obj){
		return valueOf(0, len, obj);
    }
    public static String valueOf(int st,int len,Object obj){
		String str = String.valueOf(obj);
		if (str.length() > st)
			str = str.substring(st);
		StringBuffer buf = new StringBuffer();
		if(str.length()>len)
			buf.append(str.substring(0,len));
		else
			buf.append(str);
		len = len - str.length();
		for (int i = 0; i < len; i++) {
			buf.append("-");
		}
		return buf.toString();
    }
	public static String toEncodingString(String str, String encoding) {
		if (str == null)
			return "";
		try {
			return new String(str.getBytes("ISO-8859-1"), encoding);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static String replaceTag(String str) {
		if (str != null) {
			str = str.replaceAll("<", "&lt;");
			str = str.replaceAll(">", "&gt");
//			str = str.replaceAll("&", "&amp;");
		}
		return str;
	}
	
	public static String trim(Object obj) {
		if (obj == null)
			return null;
		else
			return obj.toString().trim();
	}
	public static String toString(int[] a) {
        if (a == null)
            return "null";
        if (a.length == 0)
            return "[]";
 
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        buf.append(a[0]);
 
        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }
 
        buf.append("]");
        return buf.toString();
    }
	public static String toString(Object[] a) {
        if (a == null)
            return "null";
        if (a.length == 0)
            return "[]";
 
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        buf.append(a[0]);
 
        for (int i = 1; i < a.length; i++) {
            buf.append(", ");
            buf.append(a[i]);
        }
 
        buf.append("]");
        return buf.toString();
    }
	
	public static String toString(Object obj) {
		return ReflectUtil.toString(obj);
	}
	
	public static String toStringln(Object obj) {
		return ReflectUtil.toStringln(obj);
	}

}
