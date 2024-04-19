package com.ohs.rms.service;

import com.ohs.rms.domain.admin.Admin;
import com.ohs.rms.domain.admin.AdminRepository;
import com.ohs.rms.domain.notice.Notice;
import com.ohs.rms.domain.notice.NoticeFile;
import com.ohs.rms.domain.notice.NoticeFileRepository;
import com.ohs.rms.domain.notice.NoticeRepository;
import com.ohs.rms.dto.request.NoticeCreateRequest;
import com.ohs.rms.dto.request.NoticeFileRequest;
import com.ohs.rms.dto.request.NoticeUpdateRequest;
import com.ohs.rms.dto.response.NoticeReadResponse;
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

    @Mock
    private AdminRepository adminRepository;

    @Test
    @DisplayName("첨부파일 없이 요청 시 공지사항만 저장한다.")
    void create() {
        // given
        NoticeCreateRequest request = new NoticeCreateRequest(null, null, null, null, null);
        Notice notice = createNotice();
        Admin admin = createAdmin();

        given(adminRepository.findById(anyLong())).willReturn(Optional.of(admin));
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // then
        Long noticeId = noticeService.create(request, 1L);

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
        Admin admin = createAdmin();

        given(adminRepository.findById(anyLong())).willReturn(Optional.of(admin));
        given(noticeRepository.save(any(Notice.class))).willReturn(notice);
        given(noticeFileRepository.save(any(NoticeFile.class))).willReturn(noticeFile);

        // then
        Long noticeId = noticeService.create(request, 1L);

        // when
        assertAll(
                () -> assertThat(noticeId).isNotNull(),
                () -> assertEquals(noticeId, notice.getId()),
                () -> verify(noticeRepository, times(1)).save(any(Notice.class)),
                () -> verify(noticeFileRepository, times(1)).save(any(NoticeFile.class))
        );
    }

    @Test
    @DisplayName("선택한 id의 공지사항을 조회한다.")
    public void read() {
        // given
        Long noticeId = 1L;
        Notice notice = createNotice();

        given(noticeRepository.findById(anyLong())).willReturn(Optional.of(notice));

        // when
        NoticeReadResponse response = noticeService.read(noticeId);

        // then
        assertAll(
                () -> assertEquals(notice.getHit(), response.getHit()),
                () -> assertEquals(notice.getTitle(), response.getTitle()),
                () -> verify(noticeRepository, times(1)).findById(noticeId)
        );
    }

    @Test
    @DisplayName("올바르지 않은 id로 조회 시 예외가 발생한다.")
    public void readWithInvalidNoticeId() {
        // given
        Long noticeId = 1L;

        given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchElementException.class, () -> noticeService.read(noticeId));
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
    @DisplayName("입력한 값으로 공지사항을 수정한다.")
    public void update() {
        // given
        Long noticeId = 1L;
        NoticeUpdateRequest request = new NoticeUpdateRequest("test", "test", null, null);
        Notice notice = createNotice();

        given(noticeRepository.findById(anyLong())).willReturn(Optional.of(notice));

        // when
        noticeService.update(noticeId, request, 1L);

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
    @DisplayName("올바르지 않은 id로 수정 요청 시 예외가 발생한다.")
    public void updateWithInvalidNoticeId() {
        // given
        Long noticeId = 1L;
        NoticeUpdateRequest request = new NoticeUpdateRequest(null, null, null, null);

        given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchElementException.class, () -> noticeService.update(noticeId, request, 1L));
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
    @DisplayName("삭제 시 del 필드를 1로 변경한다.")
    public void delete() {
        // given
        Long noticeId = 1L;
        Notice notice = createNotice();

        given(noticeRepository.findById(anyLong())).willReturn(Optional.of(notice));

        // when
        noticeService.delete(noticeId, 1L);

        // then
        assertAll(
                () -> assertEquals(notice.getDel(), 1),
                () -> verify(noticeRepository, times(1)).findById(noticeId)
        );
    }

    @Test
    @DisplayName("올바르지 않은 id로 삭제 요청 시 예외가 발생한다.")
    public void deleteWithInvalidNoticeId() {
        // given
        Long noticeId = 1L;

        given(noticeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchElementException.class, () -> noticeService.delete(noticeId, 1L));
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    private Notice createNotice() {
        return Notice.builder()
                .id(1L)
                .hit(0)
                .admin(createAdmin())
                .build();
    }

    private NoticeFile createNoticeFile() {
        return NoticeFile.builder()
                .id(1L)
                .build();
    }

    private Admin createAdmin() {
        return new Admin(1L, "test");
    }
}