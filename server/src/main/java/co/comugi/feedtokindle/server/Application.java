package co.comugi.feedtokindle.server;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;

import co.comugi.feedtokindle.server.resources.FeedsResource;
import co.comugi.feedtokindle.server.resources.HtmlResource;
import co.comugi.feedtokindle.server.runnables.CheckMailRunnable;

public class Application extends ResourceConfig {
	
	private static final ScheduledExecutorService checkMailService;
	
	static {
		checkMailService = Executors.newSingleThreadScheduledExecutor();
		checkMailService.scheduleWithFixedDelay(new CheckMailRunnable(),0l,Const.CHECK_RATE_MINUTE,TimeUnit.MINUTES);
	}
	
	public Application() {
		packages("co.comugi.feedtokindle.server.resources");
		register(MoxyXmlFeature.class);
		register(MoxyJsonFeature.class);
	}
	
}