package com.ohs.rms.service;

import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFile;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeFileRepository noticeFileRepository;

    public NoticeService(NoticeRepository noticeRepository, NoticeFileRepository noticeFileRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeFileRepository = noticeFileRepository;
    }

    public Long create(NoticeCreateRequest request) {
        Notice notice = noticeRepository.save(request.toNotice());

        List<NoticeFileRequest> noticeFileRequests = request.getNoticeFileRequests();
        for (NoticeFileRequest noticeFile : noticeFileRequests) {
            noticeFileRepository.save(noticeFile.toNoticeFile(notice));
        }

        return notice.getId();
    }
}
