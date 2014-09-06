package to._2v.tools.util;

import java.util.StringTokenizer;

public class HtmlUtil {

	static String SPACE = " ";
	static String SPACE2 = "　";
	
	static String RETURN = "\r";
	static String NEWLINE = "\n";
	static String TAB = "\t";
	
	static String HTML_SPACE = "&nbsp;";
	static String HTML_NEWLINE = "<br/>";
	
	public static void convertSAPCE(String text){
		if(text==null || "".equals(text))
			return;

		text.replaceAll(SPACE, HTML_SPACE);
		text.replaceAll(SPACE2, HTML_SPACE);
	}
	
	public static String appendNLToBR(String text){
		if(text==null)
			return "";
		StringBuffer html = new StringBuffer();
		String[] strn = text.split(NEWLINE);
		for(int i=0;i<strn.length;i++){
			String[] strr = strn[i].split(RETURN);
			if(strr.length>1)
				for(int j=0;j<strr.length;j++)
					html.append(strr[j]).append(HTML_NEWLINE);
			else
				html.append(strn[i]).append(HTML_NEWLINE);
		}
		return html.toString();
	}
	public static String covertNLToBR(String text){
		if(text==null)
			return "";
		StringBuffer html = new StringBuffer();
		StringTokenizer st = new StringTokenizer(text,NEWLINE);
		while(st.hasMoreTokens()){
//			String subText = st.nextToken();
			StringTokenizer subSt = new StringTokenizer(st.nextToken(),RETURN);
			while(subSt.hasMoreTokens())
				html.append(subSt.nextToken()).append(HTML_NEWLINE);
		}
		
//		String[] strn = text.split("\n");
//		for(int i=0;i<strn.length;i++){
//			String[] strr = strn[i].split("\r");
//			if(strr.length>1)
//				for(int j=0;j<strr.length;j++)
//					html.append(strr[j]).append("<br/>");
//			else
//				html.append(strn[i]).append("<br/>");
//		}
		
		return html.toString();
	}
}
