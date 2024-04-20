package com.ohs.rms.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Notice n set n.hit = n.hit + :hitCount where n.id = :noticeId")
    void incrementHitCount(Long noticeId, Integer hitCount);
}
