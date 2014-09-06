package to._2v.tools.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import to._2v.tools.Config;

/**
 * @author Wang
 *
 */
public class BeanHelper {
	
	public static Object exeMethod(Object targetObj, String methodName){
		try {
			Method method = targetObj.getClass().getDeclaredMethod(methodName, null);
			return method.invoke(targetObj, null);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} 
	}
	
	final static String DATE = "date";
	final static String DATETIME = "datetime";
	final static String NUMERIC_DECIMAL = "decimal";
	final static String NUMERIC_ROUNDED = "rounded";
//	final static String NUMERIC_CURRENCY = "currency";
	final static DecimalFormat theDecimalFormat = new DecimalFormat("###,###.##");
	final static DecimalFormat theRoundFormat = new DecimalFormat("###,##0.00");
	static String[] chineseNumber = new String[]{"○","一","二","三","四","五","六","七","八","九","十","零"};
	static String[] chineseWeek = new String[]{"","日","一","二","三","四","五","六"};
	static String[] numbers = new String[]{"0","1","2","3","4","5","6","7","8","9"};
	static Map decimalFormats;

	public static DecimalFormat getDecimalFormat(int scale) {
		if (decimalFormats == null)
			decimalFormats = new HashMap();
		Object format = decimalFormats.get(numbers[scale]);
		if (format == null) {
			StringBuffer sb = new StringBuffer("###,##0");
			if (scale > 0)
				sb.append(".");
			for (int i = 0; i < scale; i++)
				sb.append("0");
			format = new DecimalFormat(sb.toString());
			decimalFormats.put(numbers[scale], format);
		}
		return (DecimalFormat) format;
	}
	
	public static String format(Object obj, String type) {
		if(obj==null)
			return "";
		if (type.equals(DATE)) {
			return dateFormat(obj,ISO_DATE);
		} else if (type.equals(DATETIME)) {
			return dateFormat(obj,ISO_DATETIME);
		} else if (type.equals(NUMERIC_DECIMAL)) {
			return theDecimalFormat.format(obj);
		} else if (type.equals(NUMERIC_ROUNDED)) {
			return theRoundFormat.format(obj);
		}

		return obj.toString();
	}
	
	public static String decimalFormat(float value, int scale) {
		return getDecimalFormat(scale).format(value);
	}
	
	public static String decimalFormat(double value, int scale) {
		return getDecimalFormat(scale).format(value);
	}
	
	/**
	 * eg. given decimalFormat(123456789,2) ,return 1,234,567.89
	 * @param value long
	 * @param scale number of digits to the right of the decimal point
	 * @return
	 */
	public static String scaleFormat(long value, int scale) {
		double d = 1.0;
		for (int i = 0; i < scale; i++)
			d *= 10.0;
//		System.out.println("d=" + d + "\n" + (value/d));
		return getDecimalFormat(scale).format((value/d));
	}
	
//	private static long aDayMillis = 24*60*60*100;
	public static String refreshDateFormat(Object date){
		Calendar today = Calendar.getInstance();
		if(date instanceof Date){
			Calendar datecal = (Calendar)today.clone();
			datecal.setTime((Date)date);
//			if(today.getTimeInMillis()-((Date)date).getTime() <	aDayMillis)
			if (today.get(Calendar.YEAR) == datecal.get(Calendar.YEAR)
					&& today.get(Calendar.DAY_OF_YEAR) == datecal.get(Calendar.DAY_OF_YEAR))
				return dateFormat(date,ISO_TIME);
			else if (today.get(Calendar.YEAR) == datecal.get(Calendar.YEAR))
				return dateFormat(date,NO_SLASH_DATE_ExcludeYear);
			else
				return dateFormat(date,ISO_DATE);
		}else if(date instanceof Calendar){
//			if(today.getTimeInMillis()-((Calendar)date).getTimeInMillis() <	aDayMillis)
			if (today.get(Calendar.YEAR) == ((Calendar) date).get(Calendar.YEAR)
					&& today.get(Calendar.DAY_OF_YEAR) == ((Calendar) date)
							.get(Calendar.DAY_OF_YEAR))
				return dateFormat(date,ISO_TIME);
			else if(today.get(Calendar.YEAR) == ((Calendar) date).get(Calendar.YEAR)){
				return dateFormat(date,NO_SLASH_DATE_ExcludeYear);
			}
			else
				return dateFormat(date,ISO_DATE);
		}else 
			return "";
	}
	public static String formatNumber(Object obj) {
		if (obj == null)
			return "";
		if (obj instanceof Float) {
			if (((Float) obj).floatValue() % 1 == 0)
				obj = obj.toString().substring(0, obj.toString().indexOf(".0"));
		}
		return obj.toString();
	}
	final static int ISO_TIME = 0;
	final static int ISO_DATETIME = 1;
	final static int ISO_DATE = 2;
	final static int SLASH_DATETIME = 3;
	final static int SLASH_DATE = 4;
	final static int NO_SLASH_DATETIME = 5;
	final static int NO_SLASH_DATE = 6;
	final static int ISO_DATETIME_MM = 7;
	final static int NO_SLASH_DATE_ExcludeYear = 8;
	
	static SimpleDateFormat dateFormatDashSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat dateFormatDashDay = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat dateFormatSlashSecond = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static SimpleDateFormat dateFormatSlashDay = new SimpleDateFormat("yyyy/MM/dd");
    static SimpleDateFormat dateFormatSecond = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat dateFormatDashMM = new SimpleDateFormat("MM-dd HH:mm");
    static SimpleDateFormat dateFormatDashDayExcludeYear = new SimpleDateFormat("MM-dd");
    
	public static String dateFormat(Object date,int type){
		if(date instanceof Long){
			date = new Date(((Long)date).longValue());
		}
		if(date instanceof Date){
			if(type == ISO_TIME){
				return timeFormat.format(date);
			} else if(type == ISO_DATE){
				return dateFormatDashDay.format(date);
			} else if(type == SLASH_DATETIME){
				return dateFormatSlashSecond.format(date);
			} else if(type == SLASH_DATE){
				return dateFormatSlashDay.format(date);
			} else if(type == NO_SLASH_DATETIME){
				return dateFormatSecond.format(date);
			} else if(type == NO_SLASH_DATE){
				return dateFormatDay.format(date);
			} else if(type == ISO_DATETIME_MM){
				return dateFormatDashMM.format(date);
			}else if(type == NO_SLASH_DATE_ExcludeYear){
				return dateFormatDashDayExcludeYear.format(date);
			} else{//type == ISO_DATETIME
				return dateFormatDashSecond.format(date);
			}
		} else if(date instanceof Calendar){
			return dateFormat(((Calendar)date).getTime(),type);
		} else 
			return "";
	}
	public static String chineseNumber(int num){
		return chineseNumber[num];
	}
	public static String chineseWeekDay(int day){
		return chineseWeek[day];
	}
	public static String weekDay(Calendar cal){
		return chineseWeek[cal.get(Calendar.DAY_OF_WEEK)];
//		return weekFormat(cal.getTime());
	}
	public static String nextWeekDay(Calendar cal){
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return chineseWeek[cal.get(Calendar.DAY_OF_WEEK)];
//		return weekFormat(cal.getTime());
	}
	public static int divide(int a, int b){
		return a/b;
	}
	public static String substring(String s,int beginIndex){
		return s.substring(beginIndex);
	}
	public static String substring(String s,int beginIndex,int endIndex){
		return s.substring(beginIndex, endIndex);
	}
	public static int lastIndexOf(String s,String c){
		return s.lastIndexOf(c);
	}
	public static float parseInt(String s){
		return Float.parseFloat(s);
	}
	public static String concat(Object obj1, Object obj2) {
		StringBuffer sb = new StringBuffer();
		if (obj1 != null)
			sb.append(obj1);
		if (obj2 != null)
			sb.append(obj2);
		return sb.toString();
	}
    public static List deepCopy(List src) throws IOException, ClassNotFoundException{    
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in =new ObjectInputStream(byteIn);
        List dest = (List)in.readObject();
        in.close();
        byteIn.close();
        out.close();
        byteOut.close();
        return dest;
    }
    public static int len(Object obj){
		if (obj == null)
			return 0;
		if (obj instanceof Object[])
			return ((Object[]) obj).length;
		else if (obj instanceof Collection)
			return ((Collection) obj).size();
		else if (obj instanceof Map)
			return ((Map) obj).size();
		else if (obj instanceof String)
			return ((String) obj).length();
		return 0;
    }
    
	public static long length(Object obj){
		if(obj instanceof File)
			return ((File)obj).length();
		else if(obj instanceof String)
			return new Long(((String)obj).length());
		else if(obj instanceof Number)
			return ((Number)obj).longValue();
		return 0L;
	}
    
    public static int getIntProperty(String key){
		return Config.getIntProperty(key);
	}
    
    public static double getDoubleProperty(String key){
		return Config.getDoubleProperty(key);
	}
    
    public static boolean getBooleanProperty(String key){
		return Config.getBooleanProperty(key);
	}
    
	public static String getProperty(String key){
		return Config.getProperty(key);
	}
	
	public static int count(Collection co){
		if(co!=null)
			return co.size();
		else
			return 0;
	}

	public static int count(Map map){
		if(map!=null)
			return map.size();
		else
			return 0;
	}
	
	public static Object arrays(Collection co){
		if(co!=null)
			return co.toArray();
		else
			return null;
	}
	
	public static Object keys(Map map) {
		if(map!=null)
			return map.keySet();
		else
			return null;
	}
	
	public static Object values(Map map) {
		if(map!=null)
			return map.values();
		else
			return null;
	}

	public static boolean hasKey(Map map,Object key) {
		if(map!=null)
			return map.containsKey(key);
		else
			return false;
	}
	
	public static boolean hasValue(Map map,Object value) {
		if(map!=null)
			return map.containsValue(value);
		else
			return false;
	}
	
	static float k = 1024;
	static double m = 1024 * 1024;
	static double g = k * m;
	public static String counth(long size) {
		if (size < k)
			return size + " Bytes";
		else if (size >= k && size < m) {
			return Math.round((size / k) * 10) / 10.0 + " K";
		} else if (size >= m) {
			return Math.round((size / m) * 100) / 100.0 + " M";
		}
		return size + " Bytes";
	}
}
