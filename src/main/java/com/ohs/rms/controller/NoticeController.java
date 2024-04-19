package com.ohs.rms.controller;

import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
import com.ohs.rms.service.NoticeService;
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

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody final NoticeCreateRequest noticeCreateRequest) {
        Long id = noticeService.create(noticeCreateRequest);
        return ResponseEntity.created(URI.create("/notice/" + id)).build();
    }

    @GetMapping("/{noticeId}")
    public NoticeReadResponse read(@PathVariable final Long noticeId) {
        return noticeService.read(noticeId);
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> update(@RequestBody final NoticeUpdateRequest noticeUpdateRequest,
                                       @PathVariable final Long noticeId) {
        noticeService.update(noticeId, noticeUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable final Long noticeId) {
        noticeService.delete(noticeId);
        return ResponseEntity.ok().build();
    }
}
