package com.team5.maven.common;

import org.jsoup.Jsoup;

public final class Utilities {
	public static String html2text(String html) {
		if (html != null) {
			return Jsoup.parse(html).text().replace("&", ",");
		}
		return html;
	    
	}
}
	