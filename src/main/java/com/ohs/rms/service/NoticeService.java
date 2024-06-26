package com.ohs.rms.service;

import com.ohs.rms.domain.admin.Admin;
import com.ohs.rms.domain.admin.AdminRepository;
import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeHitCounter;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
import com.ohs.rms.exception.InvalidAuthorException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final AdminRepository adminRepository;
    private final NoticeHitCounter noticeHitCounter;

    public NoticeService(NoticeRepository noticeRepository,
                         NoticeFileRepository noticeFileRepository,
                         AdminRepository adminRepository,
                         NoticeHitCounter noticeHitCounter) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.adminRepository = adminRepository;
        this.noticeHitCounter = noticeHitCounter;
    }

    @Transactional
    public Long create(NoticeCreateRequest request, Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow();
        Notice notice = noticeRepository.save(request.toNotice(admin));

        List<NoticeFileRequest> noticeFileRequests = request.getNoticeFileRequests();
        for (NoticeFileRequest noticeFile : noticeFileRequests) {
            noticeFileRepository.save(noticeFile.toNoticeFile(notice));
        }

        return notice.getId();
    }

    @Transactional
    @Cacheable(value = "notices", key = "#noticeId", condition = "#result.endAt.after(T(java.time.LocalDateTime).now())")
    public NoticeReadResponse read(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        notice.checkDeleted();

        if (notice.getEndAt().isBefore(LocalDateTime.now())) {
            notice.incrementHit();
        } else {
            noticeHitCounter.incrementHitCount(noticeId);
        }
        return NoticeReadResponse.from(notice);
    }

    @Transactional
    @CacheEvict(value = "notices", key = "#noticeId")
    public void update(Long noticeId, NoticeUpdateRequest request, Long adminId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        checkIdentification(notice.getAdmin().getId(), adminId);
        notice.update(request.getTitle(), request.getContent(), request.getStartAt(), request.getEndAt());
    }

    @Transactional
    @CacheEvict(value = "notices", key = "#noticeId")
    public void delete(Long noticeId, Long adminId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        checkIdentification(notice.getAdmin().getId(), adminId);
        notice.delete();
    }

    private void checkIdentification(Long originId, Long requestId) {
        if (!originId.equals(requestId)) {
            throw new InvalidAuthorException();
        }
    }
}
