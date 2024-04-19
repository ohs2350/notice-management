package com.ohs.rms.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohs.rms.domain.admin.Admin;
import com.ohs.rms.domain.notice.Notice;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class NoticeCreateRequest {

    @NotNull(message = "제목을 입력해주세요.")
    private String title;

    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "시작일을 입력해주세요.")
    private LocalDateTime startAt;

    @NotNull(message = "마감일을 입력해주세요.")
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

    public Notice toNotice(Admin admin) {
        return Notice.builder()
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .startAt(startAt)
                .endAt(endAt)
                .hit(0)
                .del(0)
                .admin(admin)
                .build();
    }
}
