package vook.server.api.infra.vocabulary.cache;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface UserVocabularyCacheRepository extends KeyValueRepository<UserVocabularyCache, String> {
}
