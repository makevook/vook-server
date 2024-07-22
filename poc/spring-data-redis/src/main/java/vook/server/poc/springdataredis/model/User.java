package vook.server.poc.springdataredis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import vook.server.poc.springdataredis.values.Redis;

import java.util.List;

@RedisHash(Redis.USER_KEY)
public record User(
        @Id
        String id,

        List<String> allowedVocabularies,

        @TimeToLive()
        Long timeToLive
) {
}
