package com.ohs.rms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class NoticeUpdateRequest {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "시작일을 입력해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "마감일을 입력해주세요.")
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
