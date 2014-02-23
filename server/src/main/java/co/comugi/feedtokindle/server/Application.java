package co.comugi.feedtokindle.server;

import java.util.HashSet;
import java.util.Set;

import co.comugi.feedtokindle.server.resources.FeedsResource;

public class Application extends javax.ws.rs.core.Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> r = new HashSet<Class<?>>();
		{
			r.add(FeedsResource.class);
		}
		return r;
	}
	
}
