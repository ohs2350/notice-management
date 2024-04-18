package com.ohs.rms.service;

import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeFileRepository noticeFileRepository;

    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
    }

    @Transactional
    public Long create(NoticeCreateRequest request) {
        Notice notice = noticeRepository.save(request.toNotice());

        List<NoticeFileRequest> noticeFileRequests = request.getNoticeFileRequests();
        for (NoticeFileRequest noticeFile : noticeFileRequests) {
            noticeFileRepository.save(noticeFile.toNoticeFile(notice));
        }

        return notice.getId();
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        notice.update(request.getTitle(), request.getContent(), request.getStartAt(), request.getEndAt());
    }

    @Transactional
    public void delete(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        notice.delete();
    }
}
