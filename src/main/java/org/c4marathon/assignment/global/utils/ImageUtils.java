package org.c4marathon.assignment.global.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ImageUtils {

	public static List<String> extractImageUrls(String content) {
		List<String> imageUrls = new ArrayList<>();

		Document document = Jsoup.parse(content);
		for (Element img : document.select("img")) {
			String src = img.attr("src");
			imageUrls.add(src);
		}

		return imageUrls;
	}
}
