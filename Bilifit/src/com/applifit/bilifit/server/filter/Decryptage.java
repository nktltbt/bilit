package com.applifit.bilifit.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class Decryptage implements Filter{

	private FilterConfig fConfig;
	
	@Override
	public void destroy() {
		fConfig=null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain ch) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if (req instanceof HttpServletRequest ){
			  MyRequestWrapper requestWrapper=new 
			  MyRequestWrapper((HttpServletRequest)req);
			  ch.doFilter(requestWrapper, resp);
			}else{
			   // pass the request along the filter chain
			   ch.doFilter(req, resp);
			 }
		
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		this.fConfig=fConfig;
	}

}
