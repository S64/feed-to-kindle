package co.comugi.feedtokindle.lib.readability;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParserApiResponse {
	
	@XmlElement(name="content")
	public String content;
	
	@XmlElement(name="domain")
	public String domain;
	
	@XmlElement(name="author")
	public String author;
	
	@XmlElement(name="url")
	public String url;
	
	@XmlElement(name="short_url")
	public String shortUrl;

	@XmlElement(name="title")
	public String title;

	@XmlElement(name="excerpt")
	public String excerpt;

	@XmlElement(name="direction")
	public String direction;

	@XmlElement(name="word_count")
	public long wordCount;

	@XmlElement(name="total_pages")
	public int totalPages;

	@XmlElement(name="date_published")
	public String datePublished;

	@XmlElement(name="dek")
	public String dek;

	@XmlElement(name="lead_image_url")
	public String leadImageUrl;

	@XmlElement(name="next_page_id")
	public String nextPageId;

	@XmlElement(name="rendered_pages")
	public int renderedPages;
	
}
