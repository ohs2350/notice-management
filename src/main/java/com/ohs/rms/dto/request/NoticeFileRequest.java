package com.ohs.rms.dto.request;

import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFile;
import jakarta.validation.constraints.NotNull;

public class NoticeFileRequest {

    @NotNull(message = "이름 미입력")
    private String name;

    @NotNull(message = "경로 미입력")
    private String path;

    @NotNull(message = "타입 미입력")
    private String type;

    private NoticeFileRequest() {
    }

    public NoticeFileRequest(String name, String path, String type) {
        this.name = name;
        this.path = path;
        this.type = type;
    }

    public NoticeFile toNoticeFile(Notice notice) {
        return NoticeFile.builder()
                .name(name)
                .path(path)
                .type(type)
                .notice(notice)
                .build();
    }
}
