package org.eclipse.jetty.server;

//import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.jetty.server.Utils;

@Weave(type=MatchType.Interface)
public abstract class Handler {

	@Trace(dispatcher=true)
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
		String requestURI = target;
		NewRelic.addCustomParameter("Handler Target", target);
		NewRelic.addCustomParameter("Handler BaseRequest URI", baseRequest.getRequestURI());
		NewRelic.addCustomParameter("Handler HttpServletRequest URI", request.getRequestURI());
		Utils.MatchesHolder holder = Utils.matches(requestURI);
		if(holder.matches && holder.regex != null && !holder.regex.isEmpty()) {
			requestURI = holder.regex;
		}
		NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.CUSTOM_HIGH, true, "Ring-Jetty", requestURI);
		
		Weaver.callOriginal();
	}
}
