package co.comugi.feedtokindle.server.runnables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.sun.istack.internal.ByteArrayDataSource;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import co.comugi.feedtokindle.lib.jaxb.atom.Atom;
import co.comugi.feedtokindle.lib.jaxb.atom.Entry;
import co.comugi.feedtokindle.server.Const;

public class CheckMailRunnable implements Runnable {

	public void run() {
		@SuppressWarnings("serial")
		final Type type = new TypeToken<List<String>>(){}.getType();
		final List<String> checkedUrls = new ArrayList<String>();
		{
			InputStreamReader reader = new InputStreamReader( CheckMailRunnable.class.getResourceAsStream("/properties/checked_urls.json") );
			List<String> temp = new Gson().fromJson(reader,type);
			checkedUrls.addAll(temp);
		}
		final Session session = Session.getDefaultInstance(Const.propMail,new PlainAuthenticator(Const.propMail));
		final JerseyClient client = JerseyClientBuilder.createClient();
		{
			//session.setDebug(true);
		}
		for( final String key : Const.FEEDS.keySet() ) {
			//final String feed = Const.FEEDS.get(key);
			JerseyWebTarget target = client.target(Const.SERVER_URL_BASE+Const.SERVER_API_PREFIX+"feeds/single");
			final Atom atom = target.queryParam("feed_id",key).request().get(Atom.class);
			for( Entry entry : atom.entries ) {
				final String url = entry.links.get(0).href;
				if( checkedUrls.contains(url) )
					continue;
				else
					checkedUrls.add(url);
				try {
					final MimeMultipart multiPart = new MimeMultipart();
					final MimeBodyPart bodyPart;
					final MimeBodyPart filePart;
					//final String text;
					{
						filePart = new MimeBodyPart();
						filePart.setFileName( MimeUtility.encodeText(entry.title+".html","iso-2022-jp",null) );
						final byte[] data;
						{
							final HttpClient hc = HttpClientBuilder.create().build();
							HttpGet req = new HttpGet(url);
							HttpResponse res = hc.execute(req);
							data = EntityUtils.toByteArray(res.getEntity());
						}
						filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(data,MediaType.APPLICATION_OCTET_STREAM)));
					}
					{
						bodyPart = new MimeBodyPart();
						bodyPart.setText(entry.title,"iso-2022-jp");
					}
					{
						multiPart.addBodyPart(bodyPart);
						multiPart.addBodyPart(filePart);
					}
					final MimeMessage msg = new MimeMessage(session);
					{
						msg.setFrom( Const.propMail.getProperty("mail.smtp.from") );
						msg.setRecipient(Message.RecipientType.TO,new InternetAddress( Const.KINDLE_PERSONAL_DOCUMENT_ADDRESS ));
						msg.setSubject("Convert");
						msg.setSentDate(new Date());
					}
					{
						msg.setContent(multiPart);
					}
					{
						Transport.send(msg);
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			File file = new File( CheckMailRunnable.class.getResource("/properties/checked_urls.json").toURI() );
			if( !file.isFile() || !file.canWrite() )
				file.createNewFile();
			String str = new Gson().toJson(checkedUrls,type);
			FileWriter writer = new FileWriter(file);
			writer.write(str);
			writer.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class PlainAuthenticator extends Authenticator {
		
		private final String username;
		private final String password;
		
		public PlainAuthenticator(Properties prop) {
			username = prop.getProperty("mail.smtp.username");
			password = prop.getProperty("mail.smtp.password");
		}
		
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
		
	}
	
}