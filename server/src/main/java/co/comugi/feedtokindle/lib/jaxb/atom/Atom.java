package co.comugi.feedtokindle.lib.jaxb.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang3.time.DateFormatUtils;

@XmlRootElement(name="feed")
public class Atom {
	
	@XmlAttribute(name="xmlns")
	public final String xmlns = "http://www.w3.org/2005/Atom";
	
	@XmlElement(name="title")
	public String title;
	
	@XmlElement(name="id")
	public String id;
	
	@XmlElement(name="link")
	public List<Link> links = new ArrayList<Link>();
	
	@XmlElement(name="updated")
	public String updated = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(System.currentTimeMillis());
	
	@XmlElement(name="entry")
	public List<Entry> entries = new ArrayList<Entry>();
	
}