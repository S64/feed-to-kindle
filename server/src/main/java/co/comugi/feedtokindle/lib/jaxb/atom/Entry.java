package co.comugi.feedtokindle.lib.jaxb.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="entry")
@XmlSeeAlso({
	Link.class
})
public class Entry {
	
	@XmlElement(name="title")
	public String title;
	
	@XmlElement
	public List<Link> links = new ArrayList<Link>();
	
	@XmlElement(name="content")
	public String content;
	
}
