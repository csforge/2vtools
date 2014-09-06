package to._2v.tools.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.RowList;


public class FileUtil {
	private static final Log logger = LogFactory.getLog(FileUtil.class);
//	public static boolean copy(FormFile file,File to){
//		try {
//            //retrieve the file data
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            InputStream stream = file.getInputStream();
//            String data = null;
////            if (!writeFile) {
//                //only write files out that are less than 1MB
//                if (file.getFileSize() < (4*1024000)) {
//
//                    byte[] buffer = new byte[8192];
//                    int bytesRead = 0;
//                    while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
//                        baos.write(buffer, 0, bytesRead);
//                    }
//                    data = new String(baos.toByteArray());
//                }
//                else {
//                    data = new String("The file is greater than 4MB, " +
//                            " and has not been written to stream." +
//                            " File Size: " + file.getFileSize() + " bytes. This is a" +
//                            " limitation of this particular web application, hard-coded" +
//                            " in org.apache.struts.webapp.upload.UploadAction");
//                }
////            }
////            else {
////                //write the file to the file specified
////                OutputStream bos = new FileOutputStream(theForm.getFilePath());
////                int bytesRead = 0;
////                byte[] buffer = new byte[8192];
////                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
////                    bos.write(buffer, 0, bytesRead);
////                }
////                bos.close();
////                data = "The file has been written to \"" + theForm.getFilePath() + "\"";
////            }
//            //close the stream
//            stream.close();
//        }
//        catch (FileNotFoundException fnfe) {
//            return false;
//        }
//        catch (IOException ioe) {
//            return false;
//        }
//	}
	/**
	 * 將路徑過濾掉，只傳回檔名
	 */
	public static String getFilename(String filepath) {
		String filename = null;
		filepath = filepath.replace('\\', '/');
		StringTokenizer token = new StringTokenizer(filepath, "/");
		while (token.hasMoreTokens()) {
			filename = token.nextToken();
		}
		return filename;
	}
	
	public static boolean copy(InputStream stream,File to){
		OutputStream bos = null;
		try {
                //write the file to the file specified
                bos = new FileOutputStream(to);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
        }
        catch (FileNotFoundException e) {
        	logger.error(e.getMessage(), e);
        }
        catch (IOException e) {
        	logger.error(e.getMessage(), e);
            return false;
        }finally{
        	close(bos);
			close(stream);
        }
		return true;
	}
    public static boolean copy(Reader reader, String to_file) {

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(reader);
			bw = new BufferedWriter(new FileWriter(to_file));

			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			close(br);
			close(bw);
		}
        
      return true;
    }
    public static boolean copyTextFile(File from_file, String to_file) {

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(from_file));
			bw = new BufferedWriter(new FileWriter(to_file));

			String line = null;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.newLine();
			}
			bw.flush();
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			close(br);
			close(bw);
		}
        
      return true;
    }
    public static String read(File from_file) {
		BufferedReader br = null;
		StringBuffer buf = new StringBuffer();
		try {
			br = new BufferedReader(new FileReader(from_file));
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line);
				buf.append('\n');
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			close(br);
		}
      return buf.toString();
    }
    /**
     * 取得副檔名(後綴名)
     * @param file
     * @return
     */
	public static String getSuffix(File file){
		return getSuffix(getFilename(file.getName()));
	}
	public static String getSuffix(String filename){
		if(filename!=null){
			String[] names = filename.split("\\.");
			if(names!=null && names.length>1)
				return "."+names[names.length-1];
		}
		return "";
	}
	/**
	 * 取得不含副檔名的檔案名稱(不含後綴的檔名)
	 * @param file
	 * @return
	 */
	public static String getPrefix(File file){
		return getPrefix(getFilename(file.getName()));
	}
	public static String getPrefix(String filename){
		if(filename!=null){
			int idx = filename.lastIndexOf(".");
			return filename.substring(0,idx);
		}
		return "";
	}
	
	public static List getRows(File file,String split){
		BufferedReader br = null;
		List results = new RowList();
        try{
			br = new BufferedReader(new FileReader(file));
			String line = "";
			List row = null;
			while((line=br.readLine())!=null){
				row = new RowList();
				if(split!=null){
					line = line + split+ "#";
					String[] strs = line.split(split);
					int len = strs.length;
					for(int j=0;j<len;j++){
						row.add(strs[j]);
					}
					row.remove(len-1);
				}else{
					StringTokenizer st = new StringTokenizer(line);
					while(st.hasMoreTokens())
						row.add(st.nextToken().trim());
				}
				results.add(row);
			}
        }catch(Exception e){
        	logger.error(e.getMessage(), e);
        }finally{
        	try {
				br.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
        }
        return results;
	}
	public int countComma(String str){
		int c = 0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i) == '?')
				c++;
		}
		return c;
	}
    public static void close(InputStream in){
    	try {
			in.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
    }
	public static void close(OutputStream out) {
		try {
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
    public static void close(Writer w){
    	try {
			w.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
    }
    public static void close(Reader r){
    	try {
			r.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
    }
}
