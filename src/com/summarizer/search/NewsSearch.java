/**
 * This class is responsible for scraping the web for news articles.
 * @author Arjun Bhattacharya
 */
package com.summarizer.search;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.summarizer.app.NewsSummarizerApp;

public class NewsSearch {
	final ArticleExtractor ext = ArticleExtractor.getInstance();
	
	/**
	 * This method takes in the user query and invokes the news
	 * search api to get a set of URLs
	 * @param query
	 * @return
	 */
	public Set<URL> getNewsArticles(String query) {
		HttpClient httpclient = HttpClients.createDefault();
		Set<URL> urlSet = null;

		try {
			URIBuilder builder = new URIBuilder(NewsSummarizerApp.apiUrl);

			builder.setParameter("q", query);
			builder.setParameter("count", "20");
			builder.setParameter("offset", "0");
			builder.setParameter("safeSearch", "Moderate");

			URI uri = builder.build();
			HttpGet request = new HttpGet(uri);
			request.setHeader("Ocp-Apim-Subscription-Key", NewsSummarizerApp.apiKey);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				urlSet = getURL(EntityUtils.toString(entity));
			}

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return urlSet;
	}
	
	/**
	 * This method takes the json content returned by the
	 * api and extracts the encoded URLs from it.
	 * @param json
	 * @return
	 */
	public Set<URL> getURL(String json) {
		Set<URL> urlSet = new LinkedHashSet<>();
		JSONObject obj = null;
		JSONArray arr = null;

		try {
			obj = new JSONObject(json);
			arr = obj.getJSONArray("value");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < arr.length(); i++){
			String urlVal = null;
			try {
				urlVal = splitQuery(new URL(arr.getJSONObject(i).getString("url")));
				urlSet.add(new URL(urlVal));
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return urlSet;
	}
	
	/**
	 * This method extracts the decoded URLs from the encoded URLs.
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String splitQuery(URL url) throws UnsupportedEncodingException {
		final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
		final String[] pairs = url.getQuery().split("&");
		for (String pair : pairs) {
			final int idx = pair.indexOf("=");
			final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			if (!query_pairs.containsKey(key)) {
				query_pairs.put(key, new LinkedList<String>());
			}
			final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			query_pairs.get(key).add(value);
		}
		return query_pairs.get("r").get(0);
	}
	
	/**
	 * This method extracts the text content of each article returned by the
	 * api and writes it to a text file on the disk.
	 * @param urlSet
	 * @param paraLength
	 * @param path
	 */
	public void writeContent(Set<URL> urlSet, int paraLength, String path) {
		int urlNum = 1;
		for(URL url : urlSet) {
			System.out.println("CONNECTING TO URL: "+url.toString());
			String articleText;
			try {
				articleText = ext.getText(url);
				if(articleText==null || articleText.length()<=100)
					continue;
			} catch (BoilerpipeProcessingException e1) {
				System.out.println(e1.getMessage());
				continue;
			}
			StringBuffer paraTxt = new StringBuffer();
			int paraNum = 1;
			String lines[] = articleText.split("\\r?\\n");
			for(String line : lines) {
				paraTxt.append("\n"+line);
				if(paraTxt.length()>=paraLength){
					BufferedWriter bw;
					try {
						bw = new BufferedWriter (new FileWriter (path+"/Doc_"+urlNum+"_"+paraNum+".txt"));
						bw.write(paraTxt.toString());
						bw.close();
					} catch (IOException e) {
						System.out.println(e.getMessage());
						continue;
					}
					paraTxt.setLength(0);
					paraNum++;
				}
			}
			if(paraTxt.length()>=500){
				try {
					BufferedWriter bw = new BufferedWriter (new FileWriter (path+"/Doc_"+urlNum+"_"+paraNum+".txt"));
					bw.write(paraTxt.toString());
					bw.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					continue;
				}
				paraTxt.setLength(0);
			} else if(paraTxt.length()>100 && paraTxt.length()<500) {
				try {
					BufferedWriter bw = new BufferedWriter (new FileWriter (path+"/Doc_"+urlNum+"_"+(--paraNum)+".txt", true));
					bw.append("\n\n"+paraTxt);
					bw.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					continue;
				}
				paraTxt.setLength(0);
			}
			urlNum++;
		}
	}

}