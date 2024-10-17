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

	public static List<String> extractFileNamesFromImageUrls(List<String> imageUrls) {
		return imageUrls.stream()
			.map(url -> url.substring(url.lastIndexOf('/') + 1)) // 파일명 추출
			.toList();
	}
}
