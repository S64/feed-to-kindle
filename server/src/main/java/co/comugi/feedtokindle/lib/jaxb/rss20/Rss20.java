package co.comugi.feedtokindle.lib.jaxb.rss20;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="rss")
public class Rss20 {
	
	@XmlElement(name="channel")
	public Channel channel;
	
}
