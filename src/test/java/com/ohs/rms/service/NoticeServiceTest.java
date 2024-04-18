package com.ohs.rms.service;

import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFile;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeFileRepository noticeFileRepository;

    @Test
    @DisplayName("첨부파일 없이 요청 시 공지사항만 저장한다.")
    void create() {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest(null, null, null, null, null);
        Notice notice = createNotice();
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // then
        Long noticeId = noticeService.create(request);

        // when
        assertAll(
                () -> assertThat(noticeId).isNotNull(),
                () -> assertEquals(noticeId, notice.getId()),
                () -> verify(noticeRepository, times(1)).save(any(Notice.class))
        );
    }

    @Test
    @DisplayName("첨부파일 포함하여 요청 시 공지사항과 첨부파일을 저장한다.")
    void createWithFiles() {
        // given
        NoticeFileRequest fileRequest = new NoticeFileRequest(null, null, null);
        NoticeCreateRequest request = new NoticeCreateRequest(null, null, null, null, List.of(fileRequest));
        Notice notice = createNotice();
        NoticeFile noticeFile = createNoticeFile();
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);
        given(noticeFileRepository.save(any(NoticeFile.class))).willReturn(noticeFile);

        // then
        Long noticeId = noticeService.create(request);

        // when
        assertAll(
                () -> assertThat(noticeId).isNotNull(),
                () -> assertEquals(noticeId, notice.getId()),
                () -> verify(noticeRepository, times(1)).save(any(Notice.class)),
                () -> verify(noticeFileRepository, times(1)).save(any(NoticeFile.class))
        );
    }

    @Test
    public void testUpdate_ValidNoticeId_ValidRequest_Success() {
        // given
        Long noticeId = 1L;
        NoticeUpdateRequest request = new NoticeUpdateRequest("test", "test", null, null);
        Notice notice = createNotice();

        given(noticeRepository.findById(anyLong())).willReturn(Optional.of(notice));

        // when
        noticeService.update(noticeId, request);

        // then
        assertAll(
                () -> assertEquals(request.getTitle(), notice.getTitle()),
                () -> assertEquals(request.getContent(), notice.getContent()),
                () -> assertEquals(request.getStartAt(), notice.getStartAt()),
                () -> assertEquals(request.getEndAt(), notice.getEndAt()),
                () -> verify(noticeRepository, times(1)).findById(noticeId)
        );
    }

    @Test
    public void testUpdate_InvalidNoticeId_ExceptionThrown() {
        // given
        Long noticeId = 1L;
        NoticeUpdateRequest request = new NoticeUpdateRequest(null, null, null, null);

        given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchElementException.class, () -> noticeService.update(noticeId, request));
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    private Notice createNotice() {
        return Notice.builder()
                .id(1L)
                .build();
    }

    private NoticeFile createNoticeFile() {
        return NoticeFile.builder()
                .id(1L)
                .build();
    }
}