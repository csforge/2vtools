package to._2v.tools.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.Constant;

public class DownloadServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(DownloadServlet.class);
	private static final long serialVersionUID = 4736574326590668313L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		String filename = request.getParameter("file");
		if (filename == null || "".equals(filename))
			doPost(request, response);
		
		File file = new File(getServletContext().getRealPath("/"), filename);
		String minetype = request.getParameter("type");

		print(response, file, minetype);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		Object obj = request.getAttribute(Constant.FILE);
		File file = null;
		if (obj instanceof File)
			file = (File) obj;
		else if (obj instanceof String)
			file = new File((String) obj);
		else
			return;

		print(response, file, String.valueOf(request.getAttribute(Constant.MIME_TYPE)));
	}
	
	void print(HttpServletResponse response,File file,String mimetype) throws IOException{
		if (file == null || !file.exists() || file.isDirectory())
			return;
		
		if (mimetype == null || mimetype.equals(""))
			mimetype = this.getServletContext().getMimeType(file.getName());
		if (mimetype == null || mimetype.equals(""))
			mimetype = "application/octet-stream";
		
		response.setContentType(mimetype);
		response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
		if(logger.isInfoEnabled())
			logger.info("MIME type: "+mimetype+" File: "+file.getName());
		
		if(mimetype.toLowerCase().startsWith("text"))
			writeText(response,file);
		else
			writeBinary(response,file);
	}

	void writeBinary(HttpServletResponse response,File file) throws IOException{
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buf = new byte[1024];
			while (bis.read(buf, 0, buf.length) != -1)
				bos.write(buf, 0, buf.length);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(bis!=null)
				bis.close();
			if(bos!=null)
				bos.close();
		}
	}
	
	void writeText(HttpServletResponse response,File file) throws IOException{
		BufferedReader br = null;
		PrintWriter out = null;
		try {
			br = new BufferedReader(new FileReader(file));
			out = response.getWriter();
			String line = null;
			while((line=br.readLine())!=null)
				out.write(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(br!=null)
				br.close();
			if(out!=null)
				out.close();
		}
	}
}
