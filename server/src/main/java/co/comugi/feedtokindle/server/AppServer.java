package co.comugi.feedtokindle.server;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;

import co.comugi.feedtokindle.lib.jaxb.rss20.Rss20;
import co.comugi.feedtokindle.lib.readability.ParserApiResponse;

public final class AppServer {
	
	public static final AppServer instance;
	
	static {
		instance = new AppServer();
	}
	
	public static AppServer get() {
		return instance;
	}
	
	
	
	public AppServer() {
		
	}
	
	public Rss20 getRss20(JerseyClient client, String uri) {
		return getRss20( client.target(uri) );
	}
	
	public Rss20 getRss20(JerseyWebTarget target) {
		try {
			return target.request(MediaType.APPLICATION_XML).get(Rss20.class);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ParserApiResponse getParserApiResponse(JerseyClient client, String uri){
		return getParserApiResponse( client.target( Const.READABILITY_API_BASE ).path("content/v1/parser").queryParam("token",Const.READABILITY_API_TOKEN).queryParam("url",uri) );
	}
	
	public ParserApiResponse getParserApiResponse(JerseyWebTarget target) {
		try {
			return target.request().get(ParserApiResponse.class);
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
