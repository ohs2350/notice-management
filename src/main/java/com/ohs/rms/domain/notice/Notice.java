package com.ohs.rms.domain.notice;

import com.ohs.rms.domain.admin.Admin;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "notice")
@Builder
public class Notice {

    @Id @Column(name = "notice_id")
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

//    @Column(name = "created_at", nullable = false, updatable = false)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    private int hit;

    private int del;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    protected Notice() {
    }

    public Notice(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime startAt, LocalDateTime endAt, int hit, int del, Admin admin) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.hit = hit;
        this.del = del;
        this.admin = admin;
    }

    public void read() {
        this.hit += 1;
    }

    public void update(String title, String content, LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title != null ? title : this.title;
        this.content = content != null ? content : this.content;
        this.startAt = startAt != null ? startAt : this.startAt;
        this.endAt = endAt != null ? endAt : this.endAt;
    }

    public void delete() {
        this.del = 1;
    }
}
