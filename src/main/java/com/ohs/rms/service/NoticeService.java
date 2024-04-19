package com.ohs.rms.service;

import com.ohs.rms.domain.admin.Admin;
import com.ohs.rms.domain.admin.AdminRepository;
import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
import com.ohs.rms.exception.NotAuthorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeFileRepository noticeFileRepository;

    private final AdminRepository adminRepository;

    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository, AdminRepository adminRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
        this.adminRepository = adminRepository;
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
    public NoticeReadResponse read(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        notice.read();
        return NoticeReadResponse.from(notice);
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateRequest request, Long adminId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        checkIdentification(notice.getAdmin().getId(), adminId);
        notice.update(request.getTitle(), request.getContent(), request.getStartAt(), request.getEndAt());
    }

    @Transactional
    public void delete(Long noticeId, Long adminId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow();
        checkIdentification(notice.getAdmin().getId(), adminId);
        notice.delete();
    }

    private void checkIdentification(Long originId, Long requestId) {
        if (!originId.equals(requestId)) {
            throw new NotAuthorException();
        }
    }
}
