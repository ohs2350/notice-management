package com.ohs.rms.domain.notice;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NoticeHitCounter {

    private final RedisTemplate<String, Integer> redisTemplate;

    public NoticeHitCounter(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementHitCount(Long noticeId) {
        String key = "notice:" + noticeId;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.opsForValue().set(key, 1);
        }
        redisTemplate.opsForValue().increment(key);
    }
}
