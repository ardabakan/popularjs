package popularjs.utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import popularjs.model.HtmlLink;

public class LinkExtractor {
	
	final static Logger logger = Logger.getLogger(LinkExtractor.class);

	private Pattern patternTag, patternLink;
	private Matcher matcherTag, matcherLink;

	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

	private static final String HTML_JS_TAG_PATTERN = "(//[^;^\\[^\\s^'^,^\"]+\\.js)";

	public LinkExtractor() {
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}

	public ArrayList<HtmlLink> extractLinks(String htmlContent,
			ArrayList<String> blackList) {

		ArrayList<HtmlLink> result = new ArrayList<HtmlLink>();

		matcherTag = patternTag.matcher(htmlContent);

		while (matcherTag.find()) {

			String href = matcherTag.group(1); // href
			String linkText = matcherTag.group(2); // link text

			matcherLink = patternLink.matcher(href);

			while (matcherLink.find()) {

				String link = matcherLink.group(1); // link

				boolean blackListed = false;
				
				if (blackList != null) {
					for (String temp : blackList) {

						// the link contains a blacklisted word, do not add to
						// results
						if (link.indexOf(temp) != -1) {

							blackListed = true;

							break;
						}

					}
				}

				// ignore garbage links & links that contain blacklisted words
				if (!blackListed
						&& HtmlLink.replaceInvalidChar(link).startsWith("http")) {
					result.add(new HtmlLink(link, linkText));
				}

			}

		}

		return result;

	}

	public ArrayList<String> extractJS(String htmlContent) {

		ArrayList<String> result = new ArrayList<String>();

		Pattern patternJS = Pattern.compile(HTML_JS_TAG_PATTERN);
		Matcher matcherTag = patternJS.matcher(htmlContent);

		while (matcherTag.find()) {

			String jsFound = matcherTag.group(1);

			if (!result.contains(jsFound)) {

				result.add(jsFound);

			}
			logger.info("Just detected a js file : " + jsFound);

		}

		return result;

	}
}
