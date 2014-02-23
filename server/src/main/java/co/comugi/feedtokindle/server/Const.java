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
	public static final Properties propMail;
	
	public static final String READABILITY_API_BASE = "https://readability.com/api/";
	public static final String SERVER_API_PREFIX = "api/";
	
	public static final Map<String,String> FEEDS;
	public static final String READABILITY_API_TOKEN;
	public static final String SERVER_URL_BASE;
	public static final long CHECK_RATE_MINUTE;
	public static final String KINDLE_PERSONAL_DOCUMENT_ADDRESS;
	
	static {
		{
			propConst = new Properties();
			propConf  = new Properties();
			propMail  = new Properties();
		}
		try {
			propConst.load( Const.class.getResourceAsStream("/properties/const.properties") );
			propConf.load( Const.class.getResourceAsStream("/properties/conf.properties") );
			propMail.load( Const.class.getResourceAsStream("/properties/mail.properties") );
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
		{
			READABILITY_API_TOKEN = propConst.getProperty("readability_api_token");
			SERVER_URL_BASE = propConst.getProperty("server_url_base");
			CHECK_RATE_MINUTE = Long.parseLong( propConst.getProperty("check_rate_minute",String.valueOf(5)) );
			KINDLE_PERSONAL_DOCUMENT_ADDRESS = propConf.getProperty("kindle_personal_document");
		}
	}
	
}
