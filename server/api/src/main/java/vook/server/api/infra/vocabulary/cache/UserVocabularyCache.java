package vook.server.api.infra.vocabulary.cache;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.ArrayList;
import java.util.List;

@RedisHash("user_vocabulary")
public record UserVocabularyCache(
        @Id
        String userUid,

        List<String> vocabularyUids,

        @TimeToLive
        Long ttl
) {
    public UserVocabularyCache {
        if (vocabularyUids == null) {
            vocabularyUids = new ArrayList<>();
        }
    }

    public UserVocabularyCache(
            String userUid,
            List<String> vocabularyUids
    ) {
        this(userUid, vocabularyUids, 3600L * 24 * 7);
    }
}
