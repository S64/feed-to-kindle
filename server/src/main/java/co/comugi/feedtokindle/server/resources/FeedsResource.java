package co.comugi.feedtokindle.server.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import co.comugi.feedtokindle.lib.jaxb.atom.Atom;
import co.comugi.feedtokindle.lib.jaxb.atom.Entry;
import co.comugi.feedtokindle.lib.jaxb.atom.Link;
import co.comugi.feedtokindle.lib.readability.ParserApiResponse;
import co.comugi.feedtokindle.server.Const;

@Path("/feeds")
@Produces(MediaType.APPLICATION_ATOM_XML)
public class FeedsResource {

	@GET
	@Path("/single")
	public Atom getFeed(@QueryParam("feed_id") String feedId) {
		final Atom r = new Atom();
		{
			JerseyClient client = JerseyClientBuilder.createClient();
			JerseyWebTarget target = client.target( Const.FEEDS.get(feedId) );
			{
				Response res = target.request().get();
				String str = res.readEntity(String.class);
				System.out.println(str);
				return r;
			}
			final Atom response = target.request().get(Atom.class);
			for( Entry d : response.entries ){
				final Entry entry = new Entry();
				{
					entry.title = d.title;
				}
				{
					final Link dl;
					{
						Link temp = null;
						for( Link t : d.links ) {
							if( t.rel.equals("alternate") && t.type.equals("text/html") ) {
								temp = t;
								break;
							} else {
								continue;
							}
						}
						dl = temp;
					}
					if(dl != null){
						final Link link = new Link();
						link.rel  = dl.rel;
						link.type = dl.type;
						/* * */
						link.href = dl.href;
						/* * */
					}
					{
						JerseyWebTarget rApi = client.target( Const.READABILITY_API_BASE ).path("content/v1/parser");
						ParserApiResponse parsed = rApi.queryParam("token",Const.READABILITY_API_TOKEN).queryParam("url",dl.href).request().get(ParserApiResponse.class);
						entry.content = parsed.content;
					}
				}
				r.entries.add(entry);
			}
		}
		return r;
	}
	
}
