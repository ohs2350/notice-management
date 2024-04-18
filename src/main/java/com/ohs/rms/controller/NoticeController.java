package com.ohs.rms.controller;

import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody NoticeCreateRequest noticeCreateRequest) {
        Long id = noticeService.create(noticeCreateRequest);
        return ResponseEntity.created(URI.create("/notice/" + id)).build();
    }
}
