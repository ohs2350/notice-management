package com.ohs.rms.domain.notice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("카운팅한 만큼 조회수를 증카시킨다.")
    void incrementHitCount() {
        // given
        Notice notice = Notice.builder()
                .id(1L)
                .title("test")
                .content("test")
                .createdAt(LocalDateTime.now())
                .startAt(LocalDateTime.now().minusDays(3))
                .endAt(LocalDateTime.now().plusDays(3))
                .del(0)
                .hit(10)
                .build();
        noticeRepository.save(notice);
        Long noticeId = notice.getId();
        int hitCount = 100;

        // when
        noticeRepository.incrementHitCount(noticeId, hitCount);

        // then
        Notice updatedNotice = noticeRepository.findById(noticeId).orElse(null);
        assertNotNull(updatedNotice);
        assertEquals(notice.getHit()+hitCount, updatedNotice.getHit());
    }
}