package to._2v.tools.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import to._2v.tools.Constant;


public class ReadFirstPageFilter implements Filter{
	private static Log logger = LogFactory.getLog(ReadFirstPageFilter.class);
	public void init(FilterConfig arg0) throws ServletException {
	}
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filter) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) req).getSession(true);
		Object obj = session.getAttribute(Constant.MEMBER);
		if (obj != null) {
//			Member m=(Member)obj;
			if(logger.isInfoEnabled())
				logger.info("read first page ...");
			
		}
		filter.doFilter(req, res);
	}

}
