package to._2v.tools.helper;

public class BaseValidator {
	
//	private String numberFormat = "";
	
	public boolean matches(String text, String regex) {
		if (text != null) {
			return text.matches(regex);
		}
		return true;
	}

}
