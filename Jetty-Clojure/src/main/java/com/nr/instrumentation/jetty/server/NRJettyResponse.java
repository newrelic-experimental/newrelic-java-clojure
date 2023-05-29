package com.nr.instrumentation.jetty.server;

import javax.servlet.http.HttpServletResponse;

import com.newrelic.api.agent.ExtendedResponse;
import com.newrelic.api.agent.HeaderType;

public class NRJettyResponse extends ExtendedResponse {

	private HttpServletResponse response = null;
	
	public NRJettyResponse(HttpServletResponse resp) {
		response = resp;
	}
	
	@Override
	public int getStatus() throws Exception {
		return response.getStatus();
	}

	@Override
	public String getStatusMessage() throws Exception {
		return null;
	}

	@Override
	public String getContentType() {
		return response.getContentType();
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public void setHeader(String name, String value) {
		response.addHeader(name, value);
	}

	@Override
	public long getContentLength() {
		return response.getBufferSize();
	}

}
