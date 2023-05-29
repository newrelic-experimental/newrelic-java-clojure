package ring.adapter;

import java.lang.reflect.Method;
import java.util.logging.Level;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class jetty$proxy_handler {

	public Object invoke(Object paramObject) {
		Class<?> paramClass = paramObject.getClass();
		Method method = null;
		try {
			method = paramClass.getDeclaredMethod("invoke", java.lang.Object.class);
			NewRelic.getAgent().getLogger().log(Level.FINE, "Will attempt to instrument {0}.{1}", paramClass.getName(),method);
		} catch(Exception e) {}
		if(method != null) {
			AgentBridge.instrumentation.instrument(method,"ProxyHandler");
			AgentBridge.instrumentation.retransformUninstrumentedClass(paramClass);
		}
		return Weaver.callOriginal();
	}
}
