package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.UserController;

public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		UserController userController = (UserController) ((HttpServletRequest) request).getSession()
				.getAttribute("userController");

		if (userController == null) {
			String contextPath = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/login.html");

		} else {
			HttpServletRequest rr = (HttpServletRequest) request;
			String requestUri = rr.getRequestURI();
			if (userController.isLoggedIn()) {
				if ((userController.getUser().getAdminStatus() != 1 && requestUri.endsWith("admin.html")) || requestUri.endsWith("login.html")) {
					String contextPath = ((HttpServletRequest) request).getContextPath();
					((HttpServletResponse) response).sendRedirect(contextPath + "/home.html");
				} 
				
			} else {

				if (requestUri.endsWith("home.html") || requestUri.endsWith("admin.html")) {
					String contextPath = ((HttpServletRequest) request).getContextPath();
					((HttpServletResponse) response).sendRedirect(contextPath + "/login.html");
				}
			}

		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
