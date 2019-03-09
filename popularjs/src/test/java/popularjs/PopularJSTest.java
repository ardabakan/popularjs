package popularjs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Test;

import popularjs.model.HtmlLink;
import popularjs.utility.LinkExtractor;

public class PopularJSTest {

	@Test
	public void readTestHtmls() throws IOException {

		String result = readFile("zalando.html");
		// System.out.println(result);
		result = readFile("google.html");
		// System.out.println(result);

	}

	@Test
	// this method checks if JS parsing is healthy, reads an html from resources
	// folder and if expected number of JS will be parsed?
	public void testJSParsingOK() throws IOException {

		String zalandoContent = readFile("zalando.html");

		System.out.println("Here is zalando content ---------->"
				+ zalandoContent.length());

		LinkExtractor linkExtractor = new LinkExtractor();
		ArrayList<String> links = linkExtractor.extractJS(zalandoContent);
		int jsCountOfZalando = links.size();

		assertEquals("Testing if JS parsing works : ", 12, jsCountOfZalando);
	}

	@Test
	// this method checks if link parsing is healthy, reads an html from
	// resources
	// folder and if expected number of links will be parsed?
	public void testLinksParsingOK() throws IOException {

		String googleContent = readFile("google.html");

		ArrayList<String> blackListedWords = new ArrayList<String>();

		LinkExtractor linkExtractor = new LinkExtractor();
		ArrayList<HtmlLink> links = linkExtractor.extractLinks(googleContent,
				blackListedWords);
		int jsCountOfGoogle = links.size();

		assertEquals("Testing if link parsing works : ", 67, jsCountOfGoogle);
	}

	@Test
	// will the number of parsed links decrease if i blacklist deichmann?
	public void testBlackListWorks() throws IOException {

		String googleContent = readFile("google.html");

		ArrayList<String> blackListedWords = new ArrayList<String>();
		blackListedWords.add("deichmann");

		LinkExtractor linkExtractor = new LinkExtractor();
		ArrayList<HtmlLink> links = linkExtractor.extractLinks(googleContent,
				blackListedWords);
		int jsCountOfGoogle = links.size();

		assertEquals("Testing if link parsing with blacklist works : ", 64,
				jsCountOfGoogle);
	}

	// utility method
	private String readFile(String fileName) throws IOException {

		StringBuilder result = new StringBuilder("");

		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(fileName);

		int ch;
		StringBuilder sb = new StringBuilder();
		while ((ch = inputStream.read()) != -1)
			sb.append((char) ch);

		return sb.toString();
	}
}
