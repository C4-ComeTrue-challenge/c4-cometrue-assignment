package org.c4marathon.assignment.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.c4marathon.assignment.domain.Notice;

@Data
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;

    public NoticeResponse(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }
}
