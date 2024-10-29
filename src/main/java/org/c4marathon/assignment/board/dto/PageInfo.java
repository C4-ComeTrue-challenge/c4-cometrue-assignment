package org.c4marathon.assignment.board.dto;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;
import org.c4marathon.assignment.global.utils.PageTokenUtils;

public record PageInfo<T>(
	String pageToken,
	List<T> data,
	boolean hasNext
) {
	public static <T> PageInfo<T> of(
		List<T> data,
		int exceptedSize,
		Function<T, Object> firstPageTokenFunction,
		Function<T, Object> secondPageTokenFunction
	) {
		if (data.size() <= exceptedSize) {
			return new PageInfo<>(null, data, false);
		}

		var lastValue = data.get(exceptedSize - 1);
		var pageToken = PageTokenUtils.encodePageToken(Pair.of(
			firstPageTokenFunction.apply(lastValue),
			secondPageTokenFunction.apply(lastValue)
		));
		return new PageInfo<>(pageToken, data.subList(0, exceptedSize), true);

	}
}
