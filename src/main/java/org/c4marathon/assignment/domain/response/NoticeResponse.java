package org.c4marathon.assignment.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.c4marathon.assignment.domain.Notice;

import java.time.LocalDateTime;

@Data
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdDate;

    public NoticeResponse(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdBy = notice.getBoard().getCreatedBy().getNickname();
        this.createdDate = notice.getCreatedDate();
    }
}
