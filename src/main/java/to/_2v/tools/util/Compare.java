package to._2v.tools.util;

import java.util.Calendar;
import java.util.Date;

public class Compare {

	public static boolean isEqual(Object obj1,Object obj2){
		if(obj1==null || obj2==null)
			return false;
		if (obj1 instanceof Date && obj2 instanceof Date)
			return CalendarUtil.theSameDate(obj1, obj2);
		else if (obj1 instanceof Calendar && obj2 instanceof Calendar)
			return CalendarUtil.theSameDateTime(obj1, obj2);
		else if (obj1 instanceof Number && obj2 instanceof Number)
			return Double.parseDouble(StringUtil.valueOf(obj1)) == Double
					.parseDouble(StringUtil.valueOf(obj2));
		else
			return StringUtil.isEqual(obj1, obj2);
	}
	
}
