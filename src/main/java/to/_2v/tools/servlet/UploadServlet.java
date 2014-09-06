package to._2v.tools.servlet;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.util.FileUtil;
import to._2v.tools.util.StringUtil;

public class UploadServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(UploadServlet.class);
	private static final long serialVersionUID = 6620735352911142026L;
	private static String UPLOAD_PATH;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		UPLOAD_PATH = getServletContext().getInitParameter("upload_path");
		String path = request.getParameter("path");
		String filename = request.getParameter("filename");
		// Create a factory for disk-based file items
		System.out.println("filename="+filename);
		FileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
//		factory.setSizeThreshold(yourMaxMemorySize);
//		factory.setRepository(yourTempDirectory);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// Set overall request size constraint
//		upload.setSizeMax(yourMaxRequestSize);
		boolean gvname = StringUtil.isNotBlank(filename);
		String fix = "";
		String pfilename = "";
		if (gvname){
			fix = FileUtil.getSuffix(filename);
			pfilename = FileUtil.getPrefix(filename);
		}
		try {
			List /* FileItem */items = upload.parseRequest(request);
			// Process the uploaded items
			Iterator iter = items.iterator();
			int c = 1;
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField()) {
					processFormField(item);
				} else { System.out.println("c:"+c);
					if (gvname && c > 1)
						processUploadedFile(item, path, pfilename + "(" + c + ")" + fix);
					else
						processUploadedFile(item, path, filename);
					c++;
				}
			}
		} catch (FileUploadException e1) {
			logger.warn("cannot upload file: "+e1.getMessage());
		}
		
	}
	
	void processFormField(FileItem item){
		if (item.isFormField()) {
//		    String name = item.getFieldName();
//		    String value = item.getString();
//			logger.info(name + "=" + value);
		}
	}
	
	void processUploadedFile(FileItem item, String path, String filename) {
		if (!item.isFormField()) {
			System.out.println("item.getName()="+item.getName());
			if (StringUtil.isBlank(filename))
				filename = FileUtil.getFilename(item.getName());
//		    String fieldName = item.getFieldName();
//		    String contentType = item.getContentType();
//		    long sizeInBytes = item.getSize();//bytes
			File file = null;
			if (StringUtil.isNotBlank(path))
				file = new File(getServletContext().getRealPath("/") + path, filename);
			else if (StringUtil.isNotBlank(UPLOAD_PATH))
				file = new File(UPLOAD_PATH, filename);
			else
				file = new File(getServletContext().getRealPath("/") + "/upload", filename);

			StringBuffer sb = new StringBuffer();
			try {
				item.write(file);
				sb.append("File have be uploaded: ").append(filename)
						.append(" (").append(counth(item.getSize()))
						.append(", ").append(item.getContentType()).append(")");
				if(logger.isInfoEnabled())
					logger.info(sb.toString());
			} catch (Exception e) {
				sb.append("cannot save file: ").append(filename).append(", ").append(e.getMessage());
				logger.warn(sb.toString());
			}
		}
	}

	float k = 1024;
	double m = 1024 * 1024;
	double g = k * m;
	String counth(long size) {
		if (size < k)
			return size + "Bytes";
		else if (size >= k && size < m) {
			return Math.round((size / k) * 10) / 10.0 + "K";
		} else if (size >= m) {
			return Math.round((size / m) * 100) / 100.0 + "M";
		}
		return size + "Bytes";
	}
}
