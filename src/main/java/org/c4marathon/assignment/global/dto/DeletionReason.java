package org.c4marathon.assignment.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletionReason {
	DELETED_BY_MEMBER("본인 삭제"),
	DELETED_BY_ADMIN("운영자에 의한 강제 삭제");
	private final String message;
}
