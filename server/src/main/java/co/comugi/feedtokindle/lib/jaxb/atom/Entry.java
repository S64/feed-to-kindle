package co.comugi.feedtokindle.lib.jaxb.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlType(name="entry")
public class Entry {

	@XmlElement(name="id")
	public String id;
	
	@XmlElement(name="title")
	public String title;
	
	@XmlElement(name="link")
	public List<Link> links = new ArrayList<Link>();
	
	@XmlElement(name="content")
	@XmlCDATA
	public String content;
	
}
