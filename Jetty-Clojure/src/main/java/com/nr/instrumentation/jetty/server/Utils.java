package com.nr.instrumentation.jetty.server;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Config;
import com.newrelic.api.agent.NewRelic;

public class Utils implements AgentConfigListener {
	
	private static List<String> regexs = new ArrayList<String>();
	
	private static Utils instance = null;
	private static String REGEXCONFIG = "jetty.url.regexs";
	
	static {
		
		Config config = NewRelic.getAgent().getConfig();
		String s = config.getValue(REGEXCONFIG);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Value of jetty.url.regexs is {0}", s);
		if(s != null && !s.isEmpty()) {
			regexs.clear();
			StringTokenizer st = new StringTokenizer(s, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				int index1 = token.indexOf("*");
				while(index1 > -1) {
					char c = token.charAt(index1-1);
					if(c != '.') {
						token = token.substring(0, index1) + '.' + token.substring(index1);
					}
					if(index1 < token.length()) {
						index1 = token.indexOf('*', index1+1);
					} else {
						index1 = -1;
					}
				}
				NewRelic.getAgent().getLogger().log(Level.FINE, "Added regular expression {0} to list", token);
				regexs.add(token);
			}
			
		}
		instance = new Utils();
		ServiceFactory.getConfigService().addIAgentConfigListener(instance);
		
		
		
	}

	public static class MatchesHolder {
		public boolean matches = false;
		public String regex = null;
	}
	
	
	public static MatchesHolder matches(String s) {
		MatchesHolder holder = new MatchesHolder();
		for(String regex : regexs) {
			if(s.matches(regex)) {
				holder.matches = true;
				if(!regex.contains(".*")) {
					holder.regex = regex;
				} else {
					holder.regex = regex.replace(".*", "*");
				}
				return holder;
			}
		}
		return holder;
	}
	
	public static void main(String[] args) {
		String s = "1234567";
		if(matches(s).matches) {
			System.out.println(s+" is all digits");
		} else {
			System.out.println(s+" contains one or more non-digits");
		}
		
		s = "a1234";
		if(matches(s).matches) {
			System.out.println(s+" is all digits");
		} else {
			System.out.println(s+" contains one or more non-digits");
		}
	}

	@Override
	public void configChanged(String appName, AgentConfig config) {
		String s = config.getValue(REGEXCONFIG);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Value of jetty.url.regexs is {0}", s);
		if(s != null && !s.isEmpty()) {
			regexs.clear();
			StringTokenizer st = new StringTokenizer(s, ",");
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				int index1 = token.indexOf("*");
				while(index1 > -1) {
					char c = token.charAt(index1-1);
					if(c != '.') {
						token = token.substring(0, index1) + '.' + token.substring(index1);
					}
					if(index1 < token.length()) {
						index1 = token.indexOf('*', index1+1);
					} else {
						index1 = -1;
					}
				}
				NewRelic.getAgent().getLogger().log(Level.FINE, "Added regular expression {0} to list", token);
				regexs.add(token);
			}
		}
	}
}
