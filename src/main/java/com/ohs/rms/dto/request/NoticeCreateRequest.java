package com.ohs.rms.dto.request;

import com.ohs.rms.domain.notice.Notice;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class NoticeCreateRequest {

    private String title;

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private List<NoticeFileRequest> noticeFileRequests;

    private NoticeCreateRequest() {
    }

    public NoticeCreateRequest(String title, String content, LocalDateTime startAt, LocalDateTime endAt, List<NoticeFileRequest> noticeFileRequests) {
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.endAt = endAt;
        this.noticeFileRequests = noticeFileRequests!=null ? noticeFileRequests : new ArrayList<>();
    }

    public Notice toNotice() {
        return Notice.builder()
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .startAt(startAt)
                .endAt(endAt)
                .hit(0)
                .del(0)
                .build();
    }
}
