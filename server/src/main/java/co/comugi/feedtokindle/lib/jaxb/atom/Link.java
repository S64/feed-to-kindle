package co.comugi.feedtokindle.lib.jaxb.atom;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="link")
public class Link {
	
	@XmlAttribute(name="rel")
	public String rel;
	
	@XmlAttribute(name="type")
	public String type;
	
	@XmlAttribute(name="href")
	public String href;
	
}
