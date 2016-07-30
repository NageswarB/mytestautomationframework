package com.webmailhostopia.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HttpUtil {
	
	/**
	 * method return all folders over given URL sorted alphabetically
	 * @param url
	 * @throws Exception
	 */
	public static List<String> getAllFoldersFromURL (String url) throws Exception{
		 Document doc = Jsoup.connect(url).get();
		 List<String> folders = new ArrayList<String>() ;
	        for (Element file : doc.select("a")) {
	        		if (file.attr("href").toString().indexOf("/") == file.attr("href").toString().length()-1){
	        			folders.add(file.attr("href"));
	        		}
	        }
	    java.util.Collections.sort(folders);
		return folders;	
	}
	/**
	 * method return file over URL folder according to prefix
	 * @param url - full URL where expected file resides
	 * @param prefix - the prefix of the request file
	 * @throws Exception
	 */
	public static String getURLFileByPrefix (String url, String prefix) throws Exception{
		 Document doc = Jsoup.connect(url).get();
		 String urlFile = "" ;
	        for (Element file : doc.select("a")) {
	        		if (file.attr("href").toString().startsWith(prefix) ){
	        			urlFile = file.attr("href").toString();
	        		}
	        }
		return urlFile;
		
	}
}
