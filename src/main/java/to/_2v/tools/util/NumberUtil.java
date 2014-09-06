package to._2v.tools.util;

public class NumberUtil {
	
	public static int getInt(Double value){
		if(value==null)
			return -1;
		return value.intValue();
	}
	/*
	 * value: toPrecisionString(3, 4) return "0003"
	 */
	public static String toPrecisionString(int num, int prec) {
		String str = String.valueOf(num);
		int len = str.length();
		for (int i = 0; i < prec - len; i++) {
			str = "0" + str;
		}
		return str;
	}
}
