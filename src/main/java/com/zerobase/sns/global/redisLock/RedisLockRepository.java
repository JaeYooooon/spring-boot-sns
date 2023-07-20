package com.zerobase.sns.global.redisLock;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository {

  private RedisTemplate<String, String> redisTemplate;

  public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public Boolean lock(String key) {
    return redisTemplate
        .opsForValue()
        .setIfAbsent(key, "lock", Duration.ofMillis(3_000));
  }

  public Boolean unlock(String key) {
    return redisTemplate.delete((key));
  }
}
