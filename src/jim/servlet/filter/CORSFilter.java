package jim.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {


		
//		HttpServletRequest request = (HttpServletRequest) servletRequest;

		//HttpServletResponse resp = (HttpServletResponse) servletResponse;

//		System.out.println("in the chain, at the top " + request.getRequestURL().toString());
		
		 HttpServletResponse response = (HttpServletResponse) servletResponse;
	        response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
	        response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
	        chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}
}