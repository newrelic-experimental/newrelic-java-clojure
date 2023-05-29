package com.nr.instrumentation.jetty.server;

import java.util.Enumeration;
import java.util.logging.Level;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.newrelic.api.agent.ExtendedRequest;
import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.NewRelic;
import com.nr.instrumentation.jetty.server.Utils.MatchesHolder;
public class NRJettyRequest extends ExtendedRequest {

	private HttpServletRequest request = null;
	
	public NRJettyRequest(HttpServletRequest req) {
		request = req;
	}
	
	
	@Override
	public String getRequestURI() {
		String requestURI = request.getRequestURI();
		MatchesHolder holder = Utils.matches(requestURI);
		if(holder.matches && holder.regex != null && !holder.regex.isEmpty()) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Value of getRequestURI is {0}", requestURI);
			requestURI = holder.regex;
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "Value of getRequestURI is {0}", requestURI);
		return requestURI;
	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	@Override
	public Enumeration getParameterNames() {
		return request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	@Override
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	@Override
	public String getCookieValue(String name) {
		 Cookie[] cookies = request.getCookies();
		 for(Cookie cookie : cookies) {
			 if(cookie.getName().equalsIgnoreCase(name)) {
				 return cookie.getValue();
			 }
		 }
		 return null;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

}
