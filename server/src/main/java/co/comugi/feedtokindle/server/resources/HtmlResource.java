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
import com.itextpdf.text.html.simpleparser.HTMLWorker;

@Path("/html")
//@Produces(MediaType.TEXT_HTML)
public class HtmlResource {
	
	public static final Map<String,Entry> cache;
	
	static {
		cache = Maps.newLinkedHashMap();
	}
	
	@GET
	@Path("/get.html")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public String get(@QueryParam("key") String key) {
		if( !cache.containsKey(key) )
			return null;
		final Entry entry = cache.get(key);
		Document doc = Jsoup.parseBodyFragment( entry.content );
		{
			Element charset = new Element(Tag.valueOf("meta"),"");
			charset.attr("charset","UTF-8");
			doc.head().appendChild(charset);
		}
		{
			Element title = new Element(Tag.valueOf("title"),"");
			title.text(entry.title);
			doc.head().appendChild(title);
		}
		return doc.html();
	}
	
	/*@GET
	@Path("/pdf")
	public Response getPdf(@QueryParam("key") String key) {
		if( !cache.containsKey(key) )
			return Response.status(404).build();
		HTMLWorker.parseToList(new StringReader(cache.get(key)),null);
	}*/
	
}