package com.ohs.rms.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeUpdateRequest {

    private String title;

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private NoticeUpdateRequest() {
    }

    public NoticeUpdateRequest(String title, String content, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
