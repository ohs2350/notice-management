package com.ohs.rms.service;

import com.ohs.rms.domain.notice.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FlushCacheJob {

    private final NoticeRepository noticeRepository;

    private final CacheManager cacheManager;

    private final RedisTemplate<String, Integer> redisTemplate;

    public FlushCacheJob(NoticeRepository noticeRepository, CacheManager cacheManager, RedisTemplate<String, Integer> redisTemplate) {
        this.noticeRepository = noticeRepository;
        this.cacheManager = cacheManager;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 60000)
    public void execute() {
        flushCache();
        scheduledDB();
        log.info("조회수 동기화 스케줄러 실행 완료");
    }

    private void scheduledDB() {
        Cache cache = cacheManager.getCache("notices");
        assert cache != null;
        cache.clear();
    }

    private void flushCache() {
        ScanOptions options = ScanOptions.scanOptions()
                .match("notice:"+"*")
                .count(100)
                .type(DataType.STRING)
                .build();
        Cursor<String> cursor = redisTemplate.scan(options);

        while (cursor.hasNext()) {
            String key = cursor.next();
            Long noticeId = keyToIdConverter(key);
            Integer hitCount = redisTemplate.opsForValue().get(key);
            noticeRepository.incrementHitCount(noticeId, hitCount);
            redisTemplate.delete(key);
        }
        cursor.close();
    }

    private Long keyToIdConverter(String key) {
        String[] parts = key.split(":");
        if (parts.length == 2 && "notice".equals(parts[0])) {
            try {
                return Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("잘못된 형식");
            }
        }
        throw new RuntimeException("잘못된 형식");
    }
}
