package co.comugi.feedtokindle.lib.jaxb.rss20;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="channel")
public class Channel {
	
	@XmlElement(name="title")
	public String title;
	
	@XmlElement(name="description")
	public String description;
	
	@XmlElement(name="link")
	public String link;
	
	@XmlElement(name="item")
	public List<Item> items;
	
}