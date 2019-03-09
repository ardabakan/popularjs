package popularjs.model;

import java.util.ArrayList;

public abstract class SearchEngine {
	
	String searchUrl; 

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public SearchEngine() {
		super();
	}

	public SearchEngine(String searchUrl) {
		super();
		this.searchUrl = searchUrl;
	}

	public abstract ArrayList<HtmlLink> searchTerm(String term) throws Exception;
	
}
