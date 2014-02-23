package co.comugi.feedtokindle.server.resources;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;

import co.comugi.feedtokindle.lib.jaxb.atom.Atom;
import co.comugi.feedtokindle.lib.jaxb.atom.Entry;
import co.comugi.feedtokindle.lib.jaxb.atom.Link;
import co.comugi.feedtokindle.lib.jaxb.rss20.Item;
import co.comugi.feedtokindle.lib.jaxb.rss20.Rss20;
import co.comugi.feedtokindle.lib.readability.ParserApiResponse;
import co.comugi.feedtokindle.server.Const;

@Path("/feeds")
@Produces(MediaType.APPLICATION_ATOM_XML)
public class FeedsResource {
	
	private static final Map<String,String> rdbCacheByUrl = Maps.newLinkedHashMap();
	
	@GET
	@Path("/single")
	public Atom getFeed(@QueryParam("feed_id") String feedId) {
		final Atom r = new Atom();
		{
			JerseyClient client = JerseyClientBuilder.createClient();
			JerseyWebTarget target = client.target( Const.FEEDS.get(feedId) );
			final Rss20 response = target.request(MediaType.APPLICATION_ATOM_XML).get(new GenericType<Rss20>(){});
			{
				r.title = response.channel.title;
			}
			{
				final Link self = new Link();
				{
					self.rel = "self";
					self.type = MediaType.APPLICATION_ATOM_XML;
					self.href = Const.SERVER_URL_BASE+Const.SERVER_API_PREFIX+"feeds/single?feed_id="+feedId;
				}
				r.links.add(self);
			}
			{
				r.id = r.links.get(0).href;
			}
			for( Item d : response.channel.items ){
				final Entry entry = new Entry();
				{
					entry.title = d.title;
				}
				if( !rdbCacheByUrl.containsKey(d.link) ) {
					JerseyWebTarget rApi = client.target( Const.READABILITY_API_BASE ).path("content/v1/parser");
					ParserApiResponse parsed = rApi.queryParam("token",Const.READABILITY_API_TOKEN).queryParam("url",d.link).request().get(new GenericType<ParserApiResponse>(){});
					final Document doc;
					{
						doc = Jsoup.parseBodyFragment(parsed.content,d.link);
						Elements imgs = doc.getElementsByTag("img");
						final HttpClient hc = HttpClientBuilder.create().build();
						for(Element ie : imgs) {
							HttpGet req = new HttpGet(ie.attr("src"));
							try {
								HttpResponse res = hc.execute(req);
								final byte[] byteimg = EntityUtils.toByteArray(res.getEntity());
								ie.attr("src","data:"+res.getEntity().getContentType().getValue()+";base64,"+ Base64.encodeBase64String(byteimg) );
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					rdbCacheByUrl.put(d.link,doc.body().html());
				}
				{
					entry.content = rdbCacheByUrl.get(d.link);
				}
				final String contentKey;
				{
					contentKey = DigestUtils.md5Hex( entry.content );
				}
				{
					final Link link = new Link();
					{
						link.rel  = "alternate";
						link.type = MediaType.TEXT_HTML;
						link.href = UriBuilder.fromUri(Const.SERVER_URL_BASE+Const.SERVER_API_PREFIX+"html/get.html")
								.queryParam("key",contentKey).build().toString();
						
					}
					entry.links.add(link);
				}
				{
					entry.id = entry.links.get(0).href;
				}
				{
					HtmlResource.cache.put(contentKey,entry);
				}
				r.entries.add(entry);
			}
		}
		return r;
	}
	
}
