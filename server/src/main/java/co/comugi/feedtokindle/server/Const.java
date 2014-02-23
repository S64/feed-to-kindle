package co.comugi.feedtokindle.server;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import co.comugi.feedtokindle.lib.LibConst;

public class Const extends LibConst {

	private static final Properties propConst;
	private static final Properties propConf;
	
	public static final Map<String,String> FEEDS;
	
	static {
		{
			propConst = new Properties();
			propConf  = new Properties();
		}
		try {
			propConst.load( Const.class.getResourceAsStream("/properties/const.properties") );
			propConf.load( Const.class.getResourceAsStream("/properties/conf.properties") );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		{
			final String str = propConf.getProperty("feed_list_json","{}");
			Type type = new TypeToken<Map<String,String>>(){}.getType();
			Map<String,String> urls = new Gson().fromJson(str,type);
			if( urls != null )
				FEEDS = urls;
			else
				FEEDS = new HashMap<String, String>();
		}
	}
	
}
