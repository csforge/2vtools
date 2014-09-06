package to._2v.tools.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.Constant;
import to._2v.tools.util.Configurator;
import to._2v.tools.util.DebugTool;
import to._2v.tools.util.RandomUtil;
import to._2v.tools.util.ReflectUtil;
import to._2v.tools.util.StringUtil;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = -7768445057550562451L;
	protected static String token = "token";
	protected static String encoding = null;
	protected static final String METHOD_OPTIONS = "OPTIONS";
	protected static final String METHOD_POST = "POST";
	protected static final String METHOD_PUT = "PUT";
	protected static final String METHOD_TRACE = "TRACE";
	protected static final String HEADER_IFMODSINCE = "If-Modified-Since";
	protected static final String HEADER_LASTMOD = "Last-Modified";
	private Log logger = LogFactory.getLog(this.getClass());
//	private BaseValidator validator;
	
//	synchronized public BaseValidator getValidator() {
//		if (validator == null)
//			validator = new BaseValidator();
//		return validator;
//	}

	public void init() throws ServletException {
		encoding = getInitParameter("encoding");
//		if (encoding == null || encoding.equals(""))
//			encoding = "UTF-8";
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public String getRealPath(String resource){
		return getServletContext().getRealPath(resource);
	}
	
	public void removeContextAttribute(String key) {
		getServletContext().removeAttribute(key);
	}
	
	public void setContextAttribute(String key, Object value) {
		getServletContext().setAttribute(key, value);
	}
	
	public Object getContextAttribute(String key) {
		return getServletContext().getAttribute(key);
	}
	
	public void removeAttribute(HttpServletRequest request,String key) {
		request.removeAttribute(key);
		request.getSession().removeAttribute(key);
	}
	
	public void setSessionAttribute(HttpServletRequest request,String key, Object value) {
		request.getSession().setAttribute(key, value);
	}
	
	public Object getSessionAttribute(HttpServletRequest request,String key) {
		return request.getSession().getAttribute(key);
	}
	
	public Object getAttribute(HttpServletRequest request,String key) {
		return request.getAttribute(key);
	}
	
	public void setAttribute(HttpServletRequest request,String key, Object value) {
		request.setAttribute(key, value);
	}
	
	public long getLongParameter(HttpServletRequest request, String key){
		String value = getParameter(request,key);
		if (value == null)
			return -1;
		return Long.parseLong(value);
	}

	public int getIntParameter(HttpServletRequest request, String key){
		String value = getParameter(request,key);
		if (value == null)
			return -1;
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public double getDoubleParameter(HttpServletRequest request, String key){
		String value = getParameter(request,key);
		if (value == null)
			return -1.0;
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return -1.0;
		}
	}
	
	public double getFloatParameter(HttpServletRequest request, String key){
		String value = getParameter(request,key);
		if (value == null)
			return -1.0f;
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			return -1.0f;
		}
	}
	
	public boolean getBooleanParameter(HttpServletRequest request, String key) {
		String value = getParameter(request, key);
		if (value == null)
			return false;
		if ("1".equals(value) || "true".equalsIgnoreCase(value)
				|| "yes".equalsIgnoreCase(value))
			return true;
		return false;
	}
	
	public String getParameter(HttpServletRequest request, String key) {
		String value = request.getParameter(key);
		if (value == null)
			return null;
		else
			value = value.trim();
		if (encoding != null && request.getMethod().equals("GET"))
			return StringUtil.toEncodingString(value, encoding);
		return value;
	}
	
	public String getParameter(HttpServletRequest request, String key,
			String encoding) {
		String value = request.getParameter(key);
		if (value == null)
			return null;
		else
			value = value.trim();
		return StringUtil.toEncodingString(value, encoding);
	}
	
	public String[] getParameters(HttpServletRequest request, String key) {
//		if (encoding != null && request.getMethod().equals("GET")) {
//			String[] paras = request.getParameterValues(key);
//			if (paras != null && paras.length > 0)
//				for (int i = 0; i < paras.length; i++)
//					paras[i] = StringUtil.toEncodingString(paras[i], encoding);
//			return paras;
//		}
		return request.getParameterValues(key);
	}
	
	public String getContextPath(HttpServletRequest request) {
//		String url = request.getRequestURI();
//		return url.substring(0, url.lastIndexOf('/')+1);
		return request.getContextPath()+"/";
	}
	
	public String getServletPath(HttpServletRequest request){
		return request.getServletPath();
	}
	
	public String getServletName(HttpServletRequest request){
		String path = request.getServletPath();
		return path.substring(path.lastIndexOf('/')+1);
	}
	
	public String getHeaderField(HttpServletRequest request, String key) {
		return request.getHeader(key);
	}

	public String getQueryString(HttpServletRequest request){
		return request.getQueryString();
	}
	
	public StringBuffer organizeParams(Map map) {
		StringBuffer buffer = new StringBuffer();
		boolean notFirst = false;
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry entry = (Map.Entry)it.next();
			if (notFirst) {
				buffer.append("&");
			} else {
				notFirst = true;
			}
			Object value = entry.getValue();
			buffer.append(entry.getKey()).append("=").append(encodeURL(value));
		}
		return buffer;
	}
	
	public String encodeURL(Object target) {
		String result = (target != null) ? target.toString() : "";
		try {
			result = URLEncoder.encode(result, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.warn("cannot encode " + target + " with utf-8: "	+ e.getMessage());
		}
		return result;
	}
	
	public String getPage(HttpServletRequest request) {
		if (getParameter(request, Constant.RESULT) != null)
			return getParameter(request, Constant.RESULT);
		else
			return getParameter(request, Constant.PAGE);
	}
	
	public String getMethodName(HttpServletRequest request) {
		if (getParameter(request, Constant.ACTION) != null)
			return getParameter(request, Constant.ACTION);
		else
			return getParameter(request, Constant.METHOD);
	}
	
	public Method getMethod(String methodName){
		try {
			return getClass().getDeclaredMethod(methodName,
					new Class[] { HttpServletRequest.class,HttpServletResponse.class });
		} catch (SecurityException e) {
			logger.warn("cannot find the method: " + methodName, e);
		} catch (NoSuchMethodException e) {
			logger.warn("cannot find the method: " + e.getMessage());
		}
		return null;
	}

	public Object invokeMethod(Method method, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (method != null)
				return method.invoke(this, new Object[] { request, response });
		} catch (Exception e) {
			logger.warn("cannot invoke the method: " + getMethodName(request), e);
		}
		return null;
	}
	
	public void redirect(HttpServletRequest request, HttpServletResponse response) {
		String page = getPage(request);
		try {
			if (page != null && !"".equals(page)){
				response.sendRedirect(page);
			}else{
				response.sendRedirect(request.getRequestURI());
			}
		} catch (IOException e) {
			logger.warn("cannot find the resource: " + getContextPath(request) + page, e);
		}
	}
	
	public void redirect(HttpServletResponse response, String page){
		try {
			response.sendRedirect(page);
		} catch (IOException e) {
			logger.warn("cannot find the resource: "+page, e);
		}
	}
	
	public void forward(HttpServletRequest request, HttpServletResponse response) {
		String page = getPage(request);
		try {
			if (page != null && !"".equals(page)){
				request.getRequestDispatcher(page).forward(request, response);
			}else{
				response.sendRedirect(request.getRequestURI());
			}
		} catch (Exception e) {
			logger.warn("cannot find the resource: " + page, e);
		}
	}

	public void forward(HttpServletRequest request,
			HttpServletResponse response, String page){
		try {
			request.getRequestDispatcher(page).forward(request, response);
		} catch (ServletException e) {
			logger.warn("cannot find the resource: "+page, e);
		} catch (IOException e) {
			logger.warn("cannot find the resource: "+page, e);
		}
	}
	
	public void printResource(HttpServletResponse response, String resource) {
		BufferedReader br = null;
		try {
			PrintWriter out = response.getWriter();
			InputStream is = Configurator.getResourceAsStream(resource, getClass());
			if (is == null)
				br = new BufferedReader(new FileReader(new File(
						getRealPath("/"), resource)));
			else
				br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				out.println(line);
			}
		} catch (IOException e) {
			logger.warn("cannot print the resource: " + resource, e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				logger.warn("cannot close the resource: " + br, e);
			}
		}
	}
	
	public void printContentType(HttpServletResponse response){
		printContentType(response,"utf-8");
	}
	
	public void printContentType(HttpServletResponse response, String charset) {
		response.setContentType("text/html;charset=" + charset);
		response.setCharacterEncoding(charset);
	}
	
	public void print(HttpServletResponse response, String text) {
		try {
			response.getWriter().print(text);
		} catch (IOException e) {
			logger.warn("cannot print the text: " + text, e);
		}
	}
	
	public void println(HttpServletResponse response, String text) {
		try {
			response.getWriter().println(text);
		} catch (IOException e) {
			logger.warn("cannot print the text: " + text, e);
		}
	}
	
	protected void writeJSONText(HttpServletResponse response,String text){
		if(logger.isDebugEnabled())
			logger.debug(text);
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copyProperties(Object obj, HttpServletRequest request){
		setProperties(obj, request);
//		copyProperties0(obj, request);
	}
	
	public void copyProperties0(Object obj, HttpServletRequest request){
//		Iterator it = ReflectUtil.fetchAllFields(obj);
		Iterator it = ReflectUtil.fetchAllPoolFields(obj);
		while(it.hasNext()){
			Field field = (Field)it.next();
			try {
				if (field.getType().isArray())
					ReflectUtil.setArrayValue(obj, field,  this.getParameters(request, field.getName()));
				else
					ReflectUtil.setValue(obj, field, this.getParameter(request, field.getName()));
			} catch (Exception e) {
				logger.warn("the resource: " + field, e);
			} 
		}
	}
	
	public void setProperties(Object obj, HttpServletRequest request){
//		Map table = ReflectUtil.getMethods(obj);
		Map table = ReflectUtil.getPoolMethods(obj);
		java.util.Enumeration enums = request.getParameterNames();
		while(enums.hasMoreElements()){
			String name = String.valueOf(enums.nextElement());
			String methodName = StringUtil.getSetMethodName(name);
			Method method = (Method)table.get(methodName);
			if (method != null) {
				try {
					if (method.getParameterTypes() != null && method.getParameterTypes().length == 1)
						if (method.getParameterTypes()[0].isArray())
							method.invoke(obj, new Object[] { this.getParameters(request, name) });
						else
							ReflectUtil.setValue(obj, method, this.getParameter(request, name));
				} catch (Exception e) {
					logger.warn("cannot invoke the method: " + method);
				}
			}
		}
	}
	/**
	 *  set <form> parameter to Bean ,which form camel(eg. userName) to underscore(eg. user_name)
	 * @param obj
	 * @param request
	 */
	public void copyToUnderscoreProperties(Object obj, HttpServletRequest request) {
//		Iterator it = ReflectUtil.fetchAllFields(obj);
		Iterator it = ReflectUtil.fetchAllPoolFields(obj);
		while (it.hasNext()) {
			Field field = (Field) it.next();
			try {
				if (field.getType() == String[].class)
					ReflectUtil.setObjectValue(obj, field,  this.getParameters(request, StringUtil.convertUnderscoreToCamel(field.getName())));
				else
					ReflectUtil.setValue(obj, field, this.getParameter(request, StringUtil.convertUnderscoreToCamel(field.getName())));
			} catch (Exception e) {
				logger.warn("the resource: " + field, e);
			}
		}
	}
	
	public void setToUnderscoreProperties(Object obj, HttpServletRequest request) {
//		Map table = ReflectUtil.getMethods(obj);
		Map table = ReflectUtil.getPoolMethods(obj);
		java.util.Enumeration enums = request.getParameterNames();
		while(enums.hasMoreElements()){
			String name = StringUtil.convertUnderscoreToCamel(String.valueOf(enums.nextElement()));
			String methodName = StringUtil.getSetMethodName(name);
			Method method = (Method)table.get(methodName);
			if (method != null) {
				try {
					if (method.getParameterTypes() != null && method.getParameterTypes().length == 1)
						if (method.getParameterTypes()[0].isArray())
							method.invoke(obj, new Object[] { this.getParameters(request, name) });
						else
							ReflectUtil.setValue(obj, method, this.getParameter(request, name));
				} catch (Exception e) {
					logger.warn("cannot invoke the method: " + method);
				}
			}
		}
	}
	
	/**
	 * set <form> parameter to Bean ,which form underscore parameter(eg. user_name) to camel field(eg. userName)
	 * @param obj
	 * @param request
	 */
	public void copyToCamelProperties(Object obj, HttpServletRequest request) {
//		Iterator it = ReflectUtil.fetchAllFields(obj);
		Iterator it = ReflectUtil.fetchAllPoolFields(obj);
		while (it.hasNext()) {
			Field field = (Field) it.next();
			try {
				if (field.getType() == String[].class)
					ReflectUtil.setObjectValue(obj, field,  this.getParameters(request, StringUtil.convertCamelToUnderscore(field.getName())));
				else
					ReflectUtil.setValue(obj, field, this.getParameter(request, StringUtil.convertCamelToUnderscore(field.getName())));
			} catch (Exception e) {
				logger.warn("the resource: " + field, e);
			}
		}
	}
	
	public void setToCamelProperties(Object obj, HttpServletRequest request) {
//		Map table = ReflectUtil.getMethods(obj);
		Map table = ReflectUtil.getPoolMethods(obj);
		java.util.Enumeration enums = request.getParameterNames();
		while(enums.hasMoreElements()){
			String name = StringUtil.convertCamelToUnderscore(String.valueOf(enums.nextElement()));
			String methodName = StringUtil.getSetMethodName(name);
			Method method = (Method)table.get(methodName);
			if (method != null) {
				try {
					if (method.getParameterTypes() != null && method.getParameterTypes().length == 1)
						if (method.getParameterTypes()[0].isArray())
							method.invoke(obj, new Object[] { this.getParameters(request, name) });
						else
							ReflectUtil.setValue(obj, method, this.getParameter(request, name));
				} catch (Exception e) {
					logger.warn("cannot invoke the method: " + method);
				}
			}
		}
	}
	
	public void checkToken(HttpServletRequest request,
			HttpServletResponse response, String page) {
		if (getAttribute(request, token) == null)
			this.setAttribute(request, token, RandomUtil.getNext(24));
		else if(getParameter(request, token).equals(getAttribute(request, token)))
			this.redirect(response, page);
		else
			this.setAttribute(request, token, RandomUtil.getNext(24));
			
	}
	
	protected class QueryString {
		private StringBuffer query;
		private int count;
		public QueryString(){
			query = new StringBuffer();
			count = 0;
		}
		public void add(String key,Object value){
			this.addParam(key, value);
		}
		public void addParam(String key,Object value){
			if (value == null)
				value = "";
			if(count>0)
				query.append("&");
//			else
//				query.append("?");
			query.append(key).append("=").append(value);
			count++;
		}
		public void addParams(String key, Object[] values) {
			if (values != null && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					if(count>0)
						query.append("&");
					query.append(key).append("=").append(values[i]);
					count++;
				}
			}
		}
		public void addParams(String[] keys, Object[] values) {
			if (keys != null && values != null) {
				for (int i = 0; i < keys.length; i++) {
					if(count>0)
						query.append("&");
					query.append(keys[i]).append("=").append(values[i]);
					count++;
				}
			}
		}
		public int length() {
			return count;
		}
		public String toString() {
			return query.toString();
		}
	}
	public String toBeanString(Object obj){
		return DebugTool.toBeanString(obj);
	}
	public void printBeanString(Object obj){
		DebugTool.printBeanString(obj);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws java.io.IOException, ServletException {
		Method method = null;
		String methodName = null;
		if ((methodName = getMethodName(request)) != null
				&& (method = getMethod(methodName)) != null)
			invokeMethod(method, request, response);
		else
			execute(request, response);
	}
	/**
	 * default method.(if "method"/"action" not exist)
	 * @param request
	 * @param response
	 */
	public abstract void execute(HttpServletRequest request, HttpServletResponse response);
//	public abstract boolean isValidate();
}
