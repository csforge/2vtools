package to._2v.tools.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import to._2v.tools.Constant;
import to._2v.tools.util.StringUtil;


public class CheckLoginFilter implements Filter{
	
	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession(true);
		String url = request.getRequestURI();
		int i = URLFilter(url);
		if (i > 0) {
			Object obj = session.getAttribute(Constant.MEMBER);
			if (obj == null) {
				if(url.indexOf(".do")>0)
					{res.getWriter().write("login");return;}
				else if(url.endsWith("index.jsp") || getServletName(request).equals("index.jsp")){
					HttpServletResponse response=(HttpServletResponse)res;
					response.sendRedirect("login.html");
				}else
					chain.doFilter(req, res);
			}
			else
				chain.doFilter(req, res);
		}
		else{
			chain.doFilter(req, res);
		}
	}

	private int URLFilter(String url){
		return StringUtil.isBlank(url)?0:url.endsWith("login.do")?-1:url.endsWith("login.jsp")?-1:url.endsWith("login.html")?-1:url.endsWith("register.html")?-1:url.endsWith("reset_pwd.html")?-1:url.indexOf("/css")>0?-1:url.indexOf("/js")>0?-1:url.indexOf("regist.do")>0?-1:1;
	}
	
	private String getServletName(HttpServletRequest request){
		String path = request.getServletPath();
		return path.substring(path.lastIndexOf('/')+1);
	}

}