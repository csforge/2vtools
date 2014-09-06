package to._2v.tools.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class URLUtil {
	private static final Log logger = LogFactory.getLog(URLUtil.class);
	
	public static void write(String urlstr,String filepath){
		InputStream in = null;   
        OutputStream out = null;
		try {
		 	in = new URL(urlstr).openStream();
		 	out = new FileOutputStream(new File(filepath));
		 	// Now copy bytes from the URL to the output stream
		 	byte[  ] buffer = new byte[4096];
            int bytes_read;
            while((bytes_read = in.read(buffer)) != -1)
                out.write(buffer, 0, bytes_read);

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
	static String requestUrl(String spec){
		String response = null;
		Reader in = null;
		Writer out = new StringWriter();
		try {
			in = new InputStreamReader(new URL(spec).openStream());
			char[] cbuf = new char[1024];
            int len;
            while((len = in.read(cbuf)) != -1)
            	out.write(cbuf, 0, len);
            response = out.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return response;
	}
}
