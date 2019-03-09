package popularjs.model;

import java.net.URLEncoder;
import java.util.ArrayList;

import popularjs.utility.HttpCaller;
import popularjs.utility.LinkExtractor;

public class GoogleSearchEngine extends SearchEngine {

	public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?num=50&q=";

	public static final ArrayList<String> BLACK_LIST = new ArrayList<String>();

	public GoogleSearchEngine() {

		super(GOOGLE_SEARCH_URL);

		BLACK_LIST.add(".google");
		BLACK_LIST.add(".googleusercontent");
		BLACK_LIST.add("search?q=");
		BLACK_LIST.add("/aclk?sa");

	}

	@Override
	public ArrayList<HtmlLink> searchTerm(String term) throws Exception {

		HttpCaller hc = new HttpCaller();

		String encodedQueryTerm = URLEncoder.encode(term, "UTF-8");

		String searchResponse = hc.makeGetCall(getSearchUrl()
				+ encodedQueryTerm);

		LinkExtractor linkExtractor = new LinkExtractor();

		ArrayList<HtmlLink> linksFound = linkExtractor.extractLinks(
				searchResponse, BLACK_LIST);

		return linksFound;
	}

}
