package co.comugi.feedtokindle.server.resources;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import co.comugi.feedtokindle.lib.jaxb.atom.Entry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

@Path("/html")
@Produces(MediaType.TEXT_HTML)
public class HtmlResource {
	
	public static final Map<String,Entry> cache;
	
	static {
		cache = Maps.newLinkedHashMap();
	}
	
	@GET
	@Path("/get.html")
	//@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public String get(@QueryParam("key") String key) {
		if( !cache.containsKey(key) )
			return null;
		final Entry entry = cache.get(key);
		Document doc = Jsoup.parseBodyFragment( entry.content );
		{
			doc.outputSettings().charset("UTF-8");
			doc.outputSettings().prettyPrint(true);
		}
		{
			Element charset = new Element(Tag.valueOf("meta"),"");
			charset.attr("http-equiv","Content-Type");
			charset.attr("content","text/html; charset=utf-8");
			doc.head().appendChild(charset);
		}
		{
			Element title = new Element(Tag.valueOf("title"),"");
			title.text(entry.title);
			doc.head().appendChild(title);
		}
		StringBuilder builder = new StringBuilder();
		builder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		builder.append(doc.html());
		return builder.toString();
	}
	
	/*@GET
	@Path("/pdf")
	public Response getPdf(@QueryParam("key") String key) {
		if( !cache.containsKey(key) )
			return Response.status(404).build();
		HTMLWorker.parseToList(new StringReader(cache.get(key)),null);
	}*/
	
}