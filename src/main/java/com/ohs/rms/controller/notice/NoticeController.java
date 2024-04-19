package com.ohs.rms.controller.notice;

import com.ohs.rms.controller.auth.Admin;
import com.ohs.rms.controller.auth.AdminId;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
import com.ohs.rms.service.NoticeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Admin
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid final NoticeCreateRequest noticeCreateRequest,
                                       @AdminId Long adminId) {
        Long id = noticeService.create(noticeCreateRequest, adminId);
        return ResponseEntity.created(URI.create("/notice/" + id)).build();
    }

    @GetMapping("/{noticeId}")
    public NoticeReadResponse read(@PathVariable final Long noticeId) {
        return noticeService.read(noticeId);
    }

    @Admin
    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> update(@RequestBody @Valid final NoticeUpdateRequest noticeUpdateRequest,
                                       @PathVariable final Long noticeId,
                                       @AdminId Long adminId) {
        noticeService.update(noticeId, noticeUpdateRequest, adminId);
        return ResponseEntity.ok().build();
    }

    @Admin
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable final Long noticeId,
                                       @AdminId Long adminId) {
        noticeService.delete(noticeId, adminId);
        return ResponseEntity.ok().build();
    }
}
