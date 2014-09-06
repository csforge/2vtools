package to._2v.tools.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
	public static SimpleDateFormat DATE_FORMAT_ISO = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat DATE_FORMAT_ISO2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat DATE_FORMAT_ISO3 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	public static SimpleDateFormat DATE_FORMAT_ISO_DAY = new SimpleDateFormat("yyyy-MM-dd"); 
	public static SimpleDateFormat DATE_FORMAT_ISO_MONTH_MINUTE = new SimpleDateFormat("MM-dd HH:mm");
	public static SimpleDateFormat DATE_FORMAT_SLASH = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static SimpleDateFormat DATE_FORMAT_SLASH_MONTH_MINUTE = new SimpleDateFormat("MM/dd HH:mm");
	public static SimpleDateFormat DATE_FORMAT_MONTH_MINUTE = new SimpleDateFormat("MMdd HH:mm");
	public static SimpleDateFormat DATE_FORMAT_SLASH_DAY = new SimpleDateFormat("yyyy/MM/dd");
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	public static SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("yyyyMMdd");
	public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat TIME_FORMAT_MINUTE = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat TIME_FORMAT_MINUTE_SHORT = new SimpleDateFormat("HHmm");
	public static SimpleDateFormat SIMPLE_FORMAT_DATE = new SimpleDateFormat("yyMMdd");
	public static SimpleDateFormat SIMPLE_FORMAT_DATE_SHORT = new SimpleDateFormat("MMdd");
	public static SimpleDateFormat SIMPLE_FORMAT_DATETIME = new SimpleDateFormat("yyMMddHHmm");
	public static SimpleDateFormat SIMPLE_FORMAT_DATETIME_SHORT = new SimpleDateFormat("MMddHHmm");
	public static SimpleDateFormat SIMPLE_FORMAT_DATETIME_LONG = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static long offset = Calendar.getInstance().getTimeZone().getRawOffset();
	public final static long lhour = 60 * 60 * 1000 * 1L;// 3600000L
	public final static long lday = 24 * 60 * 60 * 1000 * 1L;// 86400000L
	
	public static Calendar getCenturyEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.set((cal.get(Calendar.YEAR) / 100 + 1) * 100, 11, 31, 23, 59, 59);
		return cal;
	}
	
	public static Calendar getCenturyEndTime(int year) {
		if (year % 100 > 0)
			year = (year / 100 + 1) * 100;
		Calendar cal = Calendar.getInstance();
		cal.set(year, 11, 31, 23, 59, 59);
		return cal;
	}
	
	public static Calendar getCenturyBeginTime() {
		Calendar cal = Calendar.getInstance();
		cal.set((cal.get(Calendar.YEAR) / 100) * 100 + 1, 0, 1, 0, 0, 0);
		return cal;
	}
	
	public static Calendar getCenturyBeginTime(int year) {
		year = (year / 100 - 1) * 100 + 1;
		Calendar cal = Calendar.getInstance();
		cal.set(year, 0, 1, 0, 0, 0);
		return cal;
	}
	public static Calendar getMaxTimeInDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setMaxTime(cal);
		return cal;
	}
	public static Calendar getMinTimeInDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setMinTime(cal);
		return cal;
	}
	public static Date getMaxDateInDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setMaxTime(cal);
		return cal.getTime();
	}
	public static Calendar getMaxTimeInDay(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		setMaxTime(date);
		return date;
	}

	public static Date getMinDateInDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setMinTime(cal);
		return cal.getTime();
	}
	public static Calendar getMinTimeInDay(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		setMinTime(date);
		return date;
	}
	public static Calendar getMonthEndTime(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 23, 59, 59);
		date.add(Calendar.DAY_OF_YEAR, -1);
		return date;
	}
	
	public static Calendar getMonthEndTime(int year,int month) {
		Calendar date = Calendar.getInstance();
		date.set(year, month + 1, 1, 23, 59, 59);
		date.add(Calendar.DAY_OF_YEAR, -1);
		return date;
	}
	
	public static void setMonthEndTime(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 23, 59, 59);
		cal.add(Calendar.DAY_OF_YEAR, -1);
	}
	public static Calendar getMonthBeginTime(int year,int month) {
		Calendar date = Calendar.getInstance();
		date.set(year, month, 1, 0, 0, 0);
		return date;
	}
	public static Calendar getMonthBeginTime(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		date.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) , 1, 0, 0, 0);
		return date;
	}
	
	public static void setMonthBeginTime(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) , 1, 0, 0, 0);
	}
	
	public static Calendar getYearEndTime(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		date.set(cal.get(Calendar.YEAR) + 1, 0, 1, 23, 59, 59);
		date.add(Calendar.DAY_OF_YEAR, -1);
		return date;
	}
	
	public static void setYearEndTime(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR) + 1, 0, 1, 23, 59, 59);
		cal.add(Calendar.DAY_OF_YEAR, -1);
	}
	
	public static Calendar getYearBeginTime(Calendar cal) {
		Calendar date = (Calendar) cal.clone();
		date.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		return date;
	}
	
	public static void setYearBeginTime(Calendar cal) {
		cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
	}
	
	
	public static Date parseDateTime(Calendar theDate){
		try {
			return theDate.getTime();
		} catch (Exception e) {
			return null;
		}
	}
	public static Date parse(String pattern,String datestr){
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
		try {
			return dateformat.parse(datestr);
		} catch (Exception e) {
			return null;
		}
	}
	public static String format(String pattern, Date date) {
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
		try {
			return dateformat.format(date);
		} catch (Exception ex) {
			return null;
		}
	}
	public static String format(String pattern, long datelong) {
		SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
		Date date = new Date(datelong);
		try {
			return dateformat.format(date);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static Calendar parseROCDateMaxTime(String datestr){
		Calendar theTime = parseROCDate(datestr);
		setMaxTime(theTime);
		return theTime;
	}
	public static Calendar parseROCDateMinTime(String datestr){
		Calendar theTime = parseROCDate(datestr);
		setMinTime(theTime);
		return theTime;
	}
	/**
	 * 民國曆法
	 * @param datestr
	 * @return
	 */
	public static Calendar parseROCDate(String datestr){
		if(datestr==null || datestr.length()<6)
			return null;
		
		Calendar theTime = Calendar.getInstance();
		try {
			int year = 1970;
			int month = 0;
			int day = 0;
			if(datestr.length()==6){
				year = Integer.parseInt(datestr.substring(0,2));
				year = year + 1911;
				month = Integer.parseInt(datestr.substring(2,4));
				day = Integer.parseInt(datestr.substring(4,6));
			}else if(datestr.length()>=7){
				year = Integer.parseInt(datestr.substring(0,3));
				year = year + 1911;
				month = Integer.parseInt(datestr.substring(3,5));
				day = Integer.parseInt(datestr.substring(5,7));
			}
			theTime.set(Calendar.YEAR, year);
			theTime.set(Calendar.MONTH, month-1);
			theTime.set(Calendar.DAY_OF_MONTH, day);
			setNoonTime(theTime);
		} catch (Exception e1) {
			return null;
		}
		return theTime;
	}
	/**
	 * 民國曆法
	 * 
	 * @param datestr
	 * @return
	 */
	public static Calendar parseROCDate(String datestr, String fix) {
		Calendar theTime = Calendar.getInstance();
		try {
			String[] datearr = datestr.split(fix);
			int year = Integer.parseInt(datearr[0]);
			int month = Integer.parseInt(datearr[1]);
			int day = Integer.parseInt(datearr[2]);

			theTime.set(Calendar.YEAR, year + 1911);
			theTime.set(Calendar.MONTH, month - 1);
			theTime.set(Calendar.DAY_OF_MONTH, day);
			setNoonTime(theTime);
		} catch (Exception e1) {
			return null;
		}
		return theTime;
	}
	/**
	 * 民國曆法
	 * @param date
	 * @param fix
	 * @return
	 */
	public static String toROCDateString(Date date,String fix){
		Calendar theDate = Calendar.getInstance();
		theDate.setTime(date);
		return toROCDateString(theDate,fix);
	}
	/**
	 * 民國曆法
	 * @param date
	 * @param fix
	 * @return
	 */
	public static String toROCDateString(Calendar date,String fix){
		StringBuffer sb = new StringBuffer();
		sb.append(date.get(Calendar.YEAR)-1911).append(fix);
		int month = date.get(Calendar.MONTH)+1;
		if(month<10)
			sb.append("0").append(month).append(fix);
		else
			sb.append(month).append(fix);
		int day = date.get(Calendar.DAY_OF_MONTH);
		if(day<10)
			sb.append("0").append(day);
		else
			sb.append(day);
		return sb.toString();
	}
	public static String toDateString(DateFormat dateformat, long datelong) {
		try {
			return dateformat.format(new Date(datelong));
		} catch (Exception ex) {
			return null;
		}
	}
	public static long toDatelong(DateFormat dateformat,String datestr) {
		try {
			return dateformat.parse(datestr).getTime();
		} catch (Exception ex) {
			return -1;
		}
	}
	public static long toDatelong(String date) {
		try {
			return DATE_FORMAT_SLASH.parse(date).getTime();
		} catch (Exception e) {
			try {
				return DATE_FORMAT_SLASH_DAY.parse(date).getTime();
			} catch (Exception e1) {
				try {
					return DATE_FORMAT_DAY.parse(date).getTime();
				} catch (Exception e2) {
					return 0;
				}
			}
		}
	}
	public static String toDateString(long datelong) {
		Date date = new Date(datelong);
		try{
		return DATE_FORMAT_SLASH.format(date);
		}catch(Exception e){
			try {
				return DATE_FORMAT_SLASH_DAY.format(date);
			} catch (Exception e1) {
				try {
					return DATE_FORMAT_DAY.format(date);
				} catch (Exception e2) {
					return null;
				}
			}
		}
	}
	public static String toDateString(Object obj,boolean slash){
		if(obj instanceof Date)
			return toDateString((Date)obj,slash);
		else if(obj instanceof Calendar)
			return toDateString((Calendar)obj,slash);
		else
			return StringUtil.valueOf(obj);

	}
	public static String toDateTimeString(Object obj,boolean slash){
		if(obj instanceof Date)
			return toDateTimeString((Date)obj,slash);
		else if(obj instanceof Calendar)
			return toDateTimeString((Calendar)obj,slash);
		else
			return StringUtil.valueOf(obj);
	}
	public static String toDateString(Calendar calendar,boolean slash){
		return toDateString(calendar.getTime(),slash);
	}
	public static String toDateTimeString(Calendar calendar,boolean slash){
		return toDateTimeString(calendar.getTime(),slash);
	}
	public static String toDateString(Date theDate,boolean slash){
		try {
			if(slash)
				return DATE_FORMAT_SLASH_DAY.format(theDate);
			else
				return DATE_FORMAT_DAY.format(theDate);
		} catch (Exception ex) {
			return null;
		}
	}
	public static String toDateTimeString(Date theDate,boolean slash){
		try {
			if(slash)
				return DATE_FORMAT_SLASH.format(theDate);
			else
				return DATE_FORMAT.format(theDate);
		} catch (Exception ex) {
			return null;
		}
	}
	public static String toDateString(Date theDate){
		if(theDate==null)
			return null;
		return DATE_FORMAT_ISO_DAY.format(theDate);
	}
	public static String toDateTimeString(Date theDate){
		if(theDate==null)
			return null;
		return DATE_FORMAT_ISO.format(theDate);
	}
	public static String toDateTimeString2(Date theDate){
		if(theDate==null)
			return null;
		return DATE_FORMAT_ISO2.format(theDate);
	}
	public static String toDateTimeMMString(Date theDate){
		if(theDate==null)
			return null;
		return DATE_FORMAT_ISO_MONTH_MINUTE.format(theDate);
	}
	public static String toDateTimeMMString(Date theDate,boolean slash){
		if(theDate==null)
			return null;
		if(slash)
			return DATE_FORMAT_SLASH_MONTH_MINUTE.format(theDate);
		else
			return DATE_FORMAT_MONTH_MINUTE.format(theDate);
	}
	public static String toDateTimeMMString(Calendar theDate,boolean slash){
		return toDateTimeMMString(theDate.getTime(),slash);
	}
	public static String toTimeMinuteString(Date date){
		if(date==null)
			return null;
		return TIME_FORMAT_MINUTE.format(date);
	}
	public static String toTimeMinuteShortString(Date date){
		if(date==null)
			return null;
		return TIME_FORMAT_MINUTE_SHORT.format(date);
	}
	
	public static String toTimeString(Date date){
		if(date==null)
			return null;
		return TIME_FORMAT.format(date);
	}
	public static String toDateString(Calendar calendar) {
		if (calendar == null)
			return null;
		return toDateString(calendar.getTime());
	}
	public static String toDateTimeString(Calendar calendar){
		if (calendar == null)
			return null;
		return toDateTimeString(calendar.getTime());
	}
	public static String toDateTimeMMString(Calendar calendar){
		if (calendar == null)
			return null;
		return toDateTimeMMString(calendar.getTime());
	}
	public static String toTimeMinuteString(Calendar calendar){
		if (calendar == null)
			return null;
		return toTimeMinuteString(calendar.getTime());
	}
	public static String toTimeString(Calendar calendar){
		if(calendar==null)
			return null;
		return toTimeString(calendar.getTime());
	}
	/**
	 * "yyMMddHHmm"
	 * @param str
	 * @return
	 */
	public static Calendar parseCalendarSimpleMiddle(String str) {
		Date date = parseDateSimpleMiddle(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseDateSimpleMiddle(String str) {
		try {
			return SIMPLE_FORMAT_DATETIME.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * "MMddHHmm"
	 * @param str
	 * @return
	 */
	public static Calendar parseCalendarSimpleShort(String str) {
		Date date = parseDateSimpleShort(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			int year = theDate.get(Calendar.YEAR);
			theDate.setTime(date);
			theDate.set(Calendar.YEAR, year);
			return theDate;
		}
		return null;
	}
	public static Date parseDateSimpleShort(String str) {
		try {
			return SIMPLE_FORMAT_DATETIME_SHORT.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * "yyyyMMddHHmmss"
	 * @param str
	 * @return
	 */
	public static Calendar parseCalendarSimpleLong(String str) {
		Date date = parseDateSimpleLong(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseDateSimpleLong(String str) {
		try {
			return SIMPLE_FORMAT_DATETIME_LONG.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * "yyMMdd"
	 * @param str
	 * @return
	 */
	public static Calendar parseCalendarDaySimpleMiddle(String str) {
		Date date = parseDateDaySimpleMiddle(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseDateDaySimpleMiddle(String str) {
		try {
			return SIMPLE_FORMAT_DATE.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * "MMdd"
	 * @param str
	 * @return
	 */
	public static Calendar parseCalendarDaySimpleShort(String str) {
		Date date = parseDaySimpleShort(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			int year = theDate.get(Calendar.YEAR);
			theDate.setTime(date);
			theDate.set(Calendar.YEAR, year);
			return theDate;
		}
		return null;
	}
	public static Date parseDaySimpleShort(String str) {
		try {
			return SIMPLE_FORMAT_DATE_SHORT.parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static Calendar parseCalendar(String str) {
		Date date = parseDateTime(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseDate(String str){
		try {
			return DATE_FORMAT_ISO_DAY.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Calendar parseCalendarDay(String str) {
		Date date = parseDate(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseDateTime(String str){
		try {
			return DATE_FORMAT_ISO.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Date parseDateTime2(String str){
		try {
			return DATE_FORMAT_ISO2.parse(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static Calendar parseCalendarTime2(String str) {
		Date date = parseDateTime2(str);
		try {
			Calendar theDate = null;
			if (date != null) {
				theDate = Calendar.getInstance();
				theDate.setTime(date);
			}
			return theDate;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date parseDateTimeMM(String str){
		try {
			return DATE_FORMAT_ISO_MONTH_MINUTE.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Calendar parseCalendarMM(String str){
		Date date = parseDateTimeMM(str);
		if (date != null) {
			Calendar theDate = Calendar.getInstance();
			theDate.setTime(date);
			return theDate;
		}
		return null;
	}
	public static Date parseTimeMinute(String str){
		try {
			return TIME_FORMAT_MINUTE.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Date parseTimeMinuteShort(String str){
		try {
			return TIME_FORMAT_MINUTE_SHORT.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Calendar parseCalendarTimeMinuteShort(String str){
		Calendar theDate = Calendar.getInstance();
		try {
			theDate.setTime(parseTimeMinuteShort(str));
		} catch (Exception e) {
			return null;
		}
		return theDate;
	}
	public static Calendar parseCalendarTimeMinute(String str){
		Calendar theDate = Calendar.getInstance();
		try {
			theDate.setTime(parseTimeMinute(str));
		} catch (Exception e) {
			return null;
		}
		return theDate;
	}
	public static Date parseTime(String str){
		try {
			return TIME_FORMAT.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Calendar parseCalendarTime(String str){
		Calendar theDate = Calendar.getInstance();
		try {
			theDate.setTime(parseTime(str));
		} catch (Exception e) {
			return null;
		}
		return theDate;
	}
	public static Calendar parseCalendar(String str,boolean slash) {
		Calendar theDate = Calendar.getInstance();
		try {
			if(slash)
				theDate.setTime(DATE_FORMAT_SLASH.parse(str));
			else
				theDate.setTime(DATE_FORMAT.parse(str));
		} catch (Exception e) {
			return null;
		}
		return theDate;
	}
	public static Date parseDate(String str,boolean slash){
		try {
			if(slash)
				return DATE_FORMAT_SLASH_DAY.parse(str);
			else
				return DATE_FORMAT_DAY.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Date parseDateTime(String str,boolean slash){
		try {
			if(slash)
				return DATE_FORMAT_SLASH.parse(str);
			else
				return DATE_FORMAT.parse(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static Calendar parseCalendarDay(String str,boolean slash) {
		Calendar theDate = Calendar.getInstance();
		try {
			if(slash)
				theDate.setTime(DATE_FORMAT_SLASH_DAY.parse(str));
			else
				theDate.setTime(DATE_FORMAT_DAY.parse(str));
		} catch (Exception e) {
			return null;
		}
		return theDate;
	}
	
	public static boolean theSameDate(Object obj1,Object obj2){
		if(obj1==null || obj2==null)
			return false;
		return toDateString(obj1, false).equals(toDateString(obj2, false));
	}
	public static boolean theSameDateTime(Object obj1,Object obj2){
		if(obj1==null || obj2==null)
			return false;
		return toDateTimeString(obj1, false).equals(toDateTimeString(obj2, false));
	}
	
	public static long diffDays(Calendar cal1, Calendar cal2) {
		if(cal1==null || cal2==null)
			return -1;
	    return (cal1.getTimeInMillis() - cal2.getTimeInMillis()) / (24 * 60 * 60 * 1000);
	  }
	public static long diffDays(Date date1, Date date2) {
		if(date1==null || date2==null)
			return -1;
	    return (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000);
	  }
	public static int diffMonths(Date date1, Date date2) {
		if(date1==null || date2==null)
			return -1;
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date1);
	    int yearOne = calendar.get(Calendar.YEAR);
	    int monthOne = calendar.get(Calendar.MONDAY);

	    calendar.setTime(date2);
	    int yearTwo = calendar.get(Calendar.YEAR);
	    int monthTwo = calendar.get(Calendar.MONDAY);

	    return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	  }
	public static int diffMonths(Calendar cal1, Calendar cal2) {
		if(cal1==null || cal2==null)
			return -1;
		
	    int yearOne = cal1.get(Calendar.YEAR);
	    int monthOne = cal1.get(Calendar.MONDAY);

	    int yearTwo = cal2.get(Calendar.YEAR);
	    int monthTwo = cal2.get(Calendar.MONDAY);

	    return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	  }
	
	public static Date splitDate(String datestr,String split){
		if(datestr==null || split==null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		String[] dates = datestr.split(split);
		if(dates!=null && dates.length==3){
			try {
				calendar.setTimeInMillis(0);
				calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
				calendar.set(Calendar.MONTH, Integer.parseInt(dates[1])-1);
				calendar.set(Calendar.DATE, Integer.parseInt(dates[2]));
			} catch (NumberFormatException e) {
				return null;
			}
		}else
			return null;
		
		return calendar.getTime();
	}
	public static Date splitDateTime(String datestr,String datesplit,String timestr,String timesplit){
		if(datestr==null || datesplit==null || timestr==null || timesplit==null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		String[] dates = datestr.split(datesplit);
		if(dates!=null && dates.length==3){
			try {
				calendar.setTimeInMillis(0);
				calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
				calendar.set(Calendar.MONTH, Integer.parseInt(dates[1])-1);
				calendar.set(Calendar.DATE, Integer.parseInt(dates[2]));
				
			} catch (NumberFormatException e) {
				return null;
			}
		}else
			return null;
		
		String[] times = datestr.split(timestr);
		if(times!=null && times.length==3){
			try {
				calendar.set(Calendar.HOUR, Integer.parseInt(times[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
				calendar.set(Calendar.SECOND, Integer.parseInt(times[2]));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return calendar.getTime();
	}
	public static void setNoonTime(Calendar theTime){
		if(theTime==null)
			return;
		theTime.set(Calendar.HOUR_OF_DAY, 12);
		theTime.set(Calendar.MINUTE, 0);
		theTime.set(Calendar.SECOND, 0);
	}
	public static void setMaxTime(Calendar theTime){
		if(theTime==null)
			return;
		theTime.set(Calendar.HOUR_OF_DAY, 23);
		theTime.set(Calendar.MINUTE, 59);
		theTime.set(Calendar.SECOND, 59);
	}
	public static void setMinTime(Calendar theTime){
		if(theTime==null)
			return;
		theTime.set(Calendar.HOUR_OF_DAY, 0);
		theTime.set(Calendar.MINUTE, 0);
		theTime.set(Calendar.SECOND, 0);
	}
	public static long getTimeInMillisInDay(Calendar cal){
		return (cal.getTimeInMillis()+offset) % lday;
//		return (temp+ 8*lhour) % lday;
	}
	public static long getTimeInMillisInDay(Date date){
		return (date.getTime()+offset) % lday;
//		return (temp+ 8*lhour) % lday;
	}
}
