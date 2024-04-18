package com.ohs.rms.dto.request;

import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFile;

public class NoticeFileRequest {

    private String name;

    private String path;

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
