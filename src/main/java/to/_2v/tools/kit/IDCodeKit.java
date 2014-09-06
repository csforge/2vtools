package to._2v.tools.kit;

public class IDCodeKit {

	static int Multiplier_CN[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	static String CHECK_CODE_CN[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
	static int Multiplier_TW[] = { 1, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
	
	/**
	 * 
	 * @param code
	 * @param region TW or CN
	 * @return
	 */
	public static String getCheckCode(String code, String region) {
		if (region == null || region.equalsIgnoreCase("TW"))
			return getCheckCode_TW(code);
		return getCheckCode_CN(code);
	}
	
	/**
	 * 
	 * @param code
	 * @param region TW or CN
	 * @return
	 */
	public static boolean isValid(String code, String region) {
		if (region == null || region.equalsIgnoreCase("TW"))
			return isValid_TW(code);
		return isValid_CN(code);
	}
	
	/**
	 * 
	 * @param code 17 digital
	 * @return
	 */
	public static String getCheckCode_CN(String code) {
		int sum = 0;
		if (code.length() > 17)
			code = code.substring(0, 17);
		for (int i = 0; i < 17; i++) {
			int d = Integer.parseInt(String.valueOf(code.charAt(i)));
			sum += d * Multiplier_CN[i];
			System.out.print(d);
		}
		int remainder = sum % 11;
		return CHECK_CODE_CN[remainder];
	}
	
	/**
	 * 
	 * @param id 18 digital
	 * @return
	 */
	public static boolean isValid_CN(String id) {
		if (id.length() < 18)
			return false;
		String code = getCheckCode_CN(id);
		if (code.equals(id.substring(17, 18)))
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param code 9 digital
	 * @return
	 */
	public static String getCheckCode_TW(String code) {
		int sum = 0;
		code = Character.getNumericValue(code.charAt(0)) + code.substring(1);
		if (code.length() > 10)
			code = code.substring(0, 10);
		for (int i = 0; i < 10; i++) {
			int d = Integer.parseInt(String.valueOf(code.charAt(i)));
			sum += d * Multiplier_TW[i];
			System.out.print(d);
		}
		int remainder = sum % 10;
		if (remainder == 0)
			return String.valueOf(remainder);
		else
			return String.valueOf(10 - remainder);
	}
	
	/**
	 * 
	 * @param id 10 digital
	 * @return
	 */
	public static boolean isValid_TW(String id) {
		if (id.length() < 10)
			return false;
		String code = getCheckCode_TW(id);
		if (code.equals(id.substring(9, 10)))
			return true;
		return false;
	}
}
