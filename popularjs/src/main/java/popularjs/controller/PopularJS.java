package popularjs.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import popularjs.model.GoogleSearchEngine;
import popularjs.model.HtmlLink;
import popularjs.utility.HttpCaller;
import popularjs.utility.LinkExtractor;

public class PopularJS {

	final static Logger logger = Logger.getLogger(PopularJS.class);

	public static ConcurrentHashMap<String, LongAdder> jsScoresMap = new ConcurrentHashMap<>();
	
	public static final int  NUMBER_OF_POPULAR_LIBRARIES_TO_SHOW = 5;

	public static void main(String[] args) {

		System.out.println("Please enter your search term :");
		Scanner scanner = new Scanner(System.in);
		String searchTerm = scanner.nextLine();
		System.out
				.println("Patience please, now i will find the most popular JS libraries used for : "
						+ searchTerm);

		// String searchTerm = args[0];

		PopularJS popularJS = new PopularJS();

		ArrayList<String> topJSLibraries = popularJS
				.findTopJSLibraries(searchTerm);

	}

	public ArrayList<String> findTopJSLibraries(String searchTerm) {

		ArrayList<String> result = new ArrayList<String>();

		// this is where we keep the scores of js libraries
		// jquery : 3
		// angular.js : 17 blabla

		// initiate a google search engine
		GoogleSearchEngine googleSearchEngine = new GoogleSearchEngine();

		ArrayList<HtmlLink> googleSearchResults = new ArrayList<HtmlLink>();

		try {

			googleSearchResults = googleSearchEngine.searchTerm(searchTerm);

			ExecutorService pool = Executors.newFixedThreadPool(10);

			// Fetch source of all the pages asynchronously, make use of
			// CompletableFuture
			List<CompletableFuture<ArrayList<String>>> pageContentFutures = googleSearchResults
					.stream()
					.map(webPageLink -> parseJSLinks(webPageLink.getLinkUrl()))
					.collect(Collectors.toList());

			ArrayList<String> tempList = null;
			// obtain an arraylist of arraylists, containing list of js
			// libraries for each google search result
			for (CompletableFuture<ArrayList<String>> temp : pageContentFutures) {

				tempList = temp.get();

				if (tempList != null && !tempList.isEmpty()) {

					logger.info("This website has " + tempList.size()
							+ " js files");

					for (String tempJS : tempList) {

						this.jsScoresMap.computeIfAbsent(tempJS,
								k -> new LongAdder()).increment();

					}

				}

			}

		} catch (Exception e) {
			logger.error("Error getting top libraries " + e.getMessage());
		}

		Map<String, Integer> plainOldMap = new HashMap<>();

		for (Map.Entry<String, LongAdder> entry : this.jsScoresMap.entrySet()) {
			String key = entry.getKey().toString();
			LongAdder value = entry.getValue();
			plainOldMap.put(key, value.intValue());
			// System.out.println("key, " + key + " value " + value);
		}

		Map<String, Integer> sortedMap = plainOldMap
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(
						Collectors.toMap(Map.Entry::getKey,
								Map.Entry::getValue,
								(oldValue, newValue) -> oldValue,
								LinkedHashMap::new));

		
		
		System.out.println("Here are the most popular JS libraries corresponding your search term :");
		int i = 0;
		for (Map.Entry<String, Integer> entry : sortedMap.entrySet()) {
		
			if (i > NUMBER_OF_POPULAR_LIBRARIES_TO_SHOW) {
				break;
			}
			i++;
			String key = entry.getKey().toString();
			Integer value = entry.getValue();
			System.out.println(i + ") Name of JS Library : " + key + " and Usage Count : " + value);
		}

		System.out.println(sortedMap.toString());

		return result;

	}

	public CompletableFuture<ArrayList<String>> parseJSLinks(String url) {

		return CompletableFuture.supplyAsync(() -> {

			System.out.println("Current Thread (SupplyAsync) : "
					+ Thread.currentThread().getName());

			HttpCaller httpCaller = new HttpCaller();
			ArrayList<String> result = new ArrayList<String>();

			try {

				String tempResultHtml = httpCaller.makeGetCall(url);

				LinkExtractor linkExtractor = new LinkExtractor();

				result = linkExtractor.extractJS(tempResultHtml);

			} catch (Exception e) {
				logger.error("Error calling " + url + " : " + e.getMessage());
			}
			return result;
		});
	}
}
