package com.ohs.rms.domain.notice;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notice_file")
@Builder
public class NoticeFile {

    @Id @Column(name = "notice_file_id")
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    protected NoticeFile() {
    }

    private NoticeFile(Long id, String name, String path, String type, Notice notice) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.notice = notice;
    }
}
