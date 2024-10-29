package org.c4marathon.assignment.global.utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageTokenUtils {

	public static final String PAGE_TOKEN_FORMAT = "{}|{}";

	public static <T, R> String encodePageToken(Pair<T, R> data) {
		return Base64.encodeBase64String(
			StringUtils.format(PAGE_TOKEN_FORMAT, valueToString(data.getLeft()), valueToString(data.getRight()))
				.getBytes(StandardCharsets.UTF_8)
		);
	}

	public static <T, R> Pair<T, R> decodePageToken(String pageToken, Class<T> firstType, Class<R> secondType) {
		var decoded = new String(Base64.decodeBase64(pageToken), StandardCharsets.UTF_8);
		var parts = decoded.split("\\|", 2);
		Assert.isTrue(parts.length == 2, "invalid pageToken");
		return Pair.of(stringToValue(parts[0], firstType), stringToValue(parts[1], secondType));
	}

	private static <T> String valueToString(T value) {
		if (value instanceof LocalDateTime localDateTime) {
			return String.valueOf(localDateTime.toInstant(ZoneOffset.of("+09:00")).toEpochMilli());
		}
		return value.toString();
	}

	@SuppressWarnings("unchecked")
	private static <T> T stringToValue(String data, Class<T> clazz) {
		if (clazz == String.class) {
			return (T)data;
		} else if (clazz == Integer.class) {
			return (T)Integer.valueOf(data);
		} else if (clazz == Long.class) {
			return (T)Long.valueOf(data);
		} else if (clazz == Boolean.class) {
			return (T)Boolean.valueOf(data);
		} else if (clazz == LocalDateTime.class) {
			return (T)LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(data)), ZoneOffset.of("+09:00"));
		}

		throw new IllegalArgumentException("unsupported type - type: " + clazz);
	}
}
