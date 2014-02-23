package co.comugi.feedtokindle.server.resources;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;

import co.comugi.feedtokindle.lib.jaxb.atom.Atom;
import co.comugi.feedtokindle.server.Const;

@Path("/feeds")
@Produces(MediaType.APPLICATION_ATOM_XML)
public class FeedsResource {
	
	@GET
	@Path("/single")
	public Atom getFeed(@FormParam("feed_id") String feedId) {
		{
			JerseyClient client = new ClientConfig().getClient();
			JerseyWebTarget target = client.target( Const.FEEDS.get(feedId) );
			final Atom response = target.request().get(Atom.class);
			{
				
			}
		}
	}
	
}
