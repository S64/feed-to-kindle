package co.comugi.feedtokindle.server;

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
	
}
