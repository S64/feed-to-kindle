package co.comugi.feedtokindle.lib.jaxb.rss20;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="item")
public class Item {
	
	@XmlElement(name="title")
	public String title;
	
	@XmlElement(name="category")
	public String category;
	
	@XmlElement(name="link")
	public String link;
	
	@XmlElement(name="guid")
	public String guid;
	
	@XmlElement(name="pubDate")
	public String pubDate;
	
}
