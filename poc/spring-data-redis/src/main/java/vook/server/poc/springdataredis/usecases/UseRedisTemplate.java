package vook.server.poc.springdataredis.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vook.server.poc.springdataredis.model.User;
import vook.server.poc.springdataredis.values.Redis;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UseRedisTemplate {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TimeToLiveSupplier timeToLiveSupplier;

    public User createUser(String id, List<String> accessVocabularies) {
        User user = new User(id, accessVocabularies, timeToLiveSupplier.get());
        redisTemplate.opsForHash().put(Redis.USER_KEY, id, user);
        return user;
    }

    public User getKEY(String id) {
        return (User) redisTemplate.opsForHash().get(Redis.USER_KEY, id);
    }

    public void deleteUser(String id) {
        redisTemplate.opsForHash().delete(Redis.USER_KEY, id);
    }
}
