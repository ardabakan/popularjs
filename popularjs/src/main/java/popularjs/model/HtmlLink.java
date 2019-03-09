package popularjs.model;

public class HtmlLink {

	String linkUrl;

	String linkText;

	public HtmlLink(String linkUrl, String linkText) {
		super();
		this.linkUrl = replaceInvalidChar(linkUrl);
		this.linkText = linkText;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public static String replaceInvalidChar(String link) {
		link = link.replaceAll("'", "");
		link = link.replaceAll("\"", "");
		return link;
	}

}
