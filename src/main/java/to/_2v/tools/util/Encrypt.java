package to._2v.tools.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Encrypt {
	private static final Log logger = LogFactory.getLog(Encrypt.class);
	public static String MD5 = "MD5";
	public static String SHA = "SHA";

	public static String toHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		String plainText;

		for (int i = 0; i < b.length; i++) {
			plainText = Integer.toHexString(0xFF & b[i]);

			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	public static String toHexString(String str, String algorithm) {
		return toHexString(toChapterDigest(str, algorithm));
	}

	public static boolean isEqual(byte[] digesta, byte[] digestb) {
		return MessageDigest.isEqual(digesta, digestb);
	}

	public static boolean isEqual(MessageDigest mda, MessageDigest mdb) {
		return MessageDigest.isEqual(mda.digest(), mdb.digest());
	}

	public static boolean isEqual(String stra, String strb) {
		return MessageDigest.isEqual(toChapterDigest(stra),
				toChapterDigest(strb));
	}

	public static byte[] toChapterDigest(String str) {
		return toChapterDigest(str, MD5);
	}

	public static byte[] toChapterDigest(String str, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(str.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
			return null;
		}

	}
}
