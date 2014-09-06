package to._2v.tools.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadUtil {

	/**
	 * pure Java Client-Server
	 * @param toUrl
	 * @param is
	 */
	public static void upload(String toUrl,InputStream is){
		HttpURLConnection conn = null;
		OutputStream os = null;
		try {
			URL url = new URL(toUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setChunkedStreamingMode(0);

			os = new BufferedOutputStream(conn.getOutputStream());
			byte[] buf = new byte[128];
			while(is.read(buf)!=-1)
				os.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(os!=null)
					os.close();
				if(is!=null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
	}
	/**
	 * with any HTTP Client-Server
	 * @param toUrl
	 * @param files
	 * @param names
	 * @return
	 */
	public static String uploadByHttpClient(String toUrl, File[] files, String[] names) {
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//            HttpPost httppost = new HttpPost(toUrl);
//
//            MultipartEntity reqEntity = new MultipartEntity();
//            for (int i = 0; i < files.length; i++)
//            	reqEntity.addPart(names[i], new FileBody(files[i]));
////            reqEntity.addPart("filename", new StringBody(file.getName()));
//
//            httppost.setEntity(reqEntity);
//            System.out.println("executing request " + httppost.getRequestLine());
//            HttpResponse response = httpclient.execute(httppost);
//            
//            //response
//            System.out.println(response.getStatusLine());
//            HttpEntity resEntity = response.getEntity();
//            if (resEntity != null) {
//    			return getContent(resEntity.getContent());
//            }
//        } catch(IOException e){
//        	e.printStackTrace();
//        } finally {
//            // When HttpClient instance is no longer needed,
//            // shut down the connection manager to ensure
//            // immediate deallocation of all system resources
//            httpclient.getConnectionManager().shutdown();
//        }
		return null;
	}
	
	/**
	 * with any HTTP Client-Server
	 * @param toUrl
	 * @param files
	 * @param names
	 * @return
	 */
	public static String uploadUseFormFile(String toUrl, File[] files, String[] names) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****jerry2";
		
		HttpURLConnection conn = null;
		InputStream is = null;
		DataOutputStream ds = null;		
		try {
			URL url = new URL(toUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setChunkedStreamingMode(0);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-length", String.valueOf(getLength(files)));
			conn.setRequestProperty("Content-type", "multipart/form-data; boundary="+boundary);
			/*			
			Content-type: multipart/form-data; boundary=AaB03x
			--AaB03x
	        content-disposition: form-data; name="pics"; filename="file1.txt"
	        Content-Type: text/plain

	         ... contents of file1.txt ...
	        --AaB03x--
	        */
			ds = new DataOutputStream(conn.getOutputStream());
			for (int i = 0; i < files.length; i++) {
				is = new FileInputStream(files[i]);
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("content-disposition: form-data; name=\""+names[i]+"\"; filename=\""+files[i].getName()+"\""+end);
				ds.writeBytes(end);
				byte[] buf = new byte[128];
				int len = -1;
				while ((len=is.read(buf)) != -1)
					ds.write(buf, 0, len);
				ds.writeBytes(end);
			}
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			
			//response
			System.out.println("code="+conn.getResponseCode()+",msg="+conn.getResponseMessage());
			return getContent(conn.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}  finally{
			try {
				if (is != null)
					is.close();
				if (ds != null)
					ds.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return null;
	}
	
	public static String getContent(InputStream is){
		StringBuffer response = new StringBuffer();
		BufferedReader rs = null;
		try {
			rs = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = rs.readLine()) != null)
				response.append(line);
			
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (is != null)
					is.close();
				if (rs != null)
					rs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	static long getLength(File[] files) {
		long len = 0;
		for (int i = 0; i < files.length; i++)
			len += files[i].length();
		return len;
	}
}
