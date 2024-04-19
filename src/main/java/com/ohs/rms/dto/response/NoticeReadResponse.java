package com.ohs.rms.dto.response;

import com.ohs.rms.domain.notice.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeReadResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private int hit;

    private String writer;

    private NoticeReadResponse() {
    }

    private NoticeReadResponse(String title, String content, LocalDateTime createdAt, int hit, String writer) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.hit = hit;
        this.writer = writer;
    }

    public static NoticeReadResponse from(Notice notice) {
        return new NoticeReadResponse(
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt(),
                notice.getHit(),
                notice.getAdmin().getName()
        );
    }
}
