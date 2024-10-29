package org.c4marathon.assignment.global.utils;

import org.slf4j.helpers.MessageFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

	/**
	 * 형식과 객체가 주어졌을 때 , 해당 형식에 맞춰 객체를 보낸다.
	 * @param format
	 * @param object
	 * @return
	 */
	public static String format(String format, Object... object) {
		return MessageFormatter.arrayFormat(format, object).getMessage();
	}
}
