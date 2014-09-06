package to._2v.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compress {
	static final int BUFFER = 1024;
	
	public static void zip(File dest,String src){
		zip(dest,new File(src));
	}
	public static void zip(String dest,String src){
		zip(new File(dest),new File(src));
	}
	public static void zip(File dest, File src) {
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(dest));
			zip(out, src, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void zip(ZipOutputStream out, File file, String base)
			throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < files.length; i++) {
				zip(out, files[i], base + files[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream fis = new FileInputStream(file);
			byte buf[] = new byte[BUFFER];
			while (fis.read(buf, 0, buf.length) != -1) {
				out.write(buf, 0, buf.length);
			}
			fis.close();
		}
	}
}
