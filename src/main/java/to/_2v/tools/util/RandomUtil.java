package to._2v.tools.util;


public class RandomUtil {

	public static String getNext(){
		return getNext(16);
	}
	
	public static String getNext(int size) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++)
			sb.append(getRandomChar());
		return sb.toString();
	}

	public static String getRandomChar() {
		int rand = (int) Math.round(Math.random() * 1);
		long itmp = 0;
		char ctmp = '\u0000';
		switch (rand) {
		case 1: // 生成英文的情況
			itmp = Math.round(Math.random() * 25 + 65);  //97
			ctmp = (char) itmp;
			return String.valueOf(ctmp);
		default: // 生成數字的情況
			itmp = Math.round(Math.random() * 9);
			return String.valueOf(itmp);
		}
	}
}
