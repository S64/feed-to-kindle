package co.comugi.feedtokindle.lib.jaxb.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(name="feed")
@XmlSeeAlso({
	Entry.class
})
public class Atom {
	
	@XmlElement
	public List<Entry> entries = new ArrayList<Entry>();
	
}
