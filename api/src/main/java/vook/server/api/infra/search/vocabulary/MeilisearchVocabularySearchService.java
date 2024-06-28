package vook.server.api.infra.search.vocabulary;

import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import org.springframework.stereotype.Service;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularySearchService;
import vook.server.api.infra.search.common.MeilisearchProperties;
import vook.server.api.infra.search.common.MeilisearchService;

@Service
public class MeilisearchVocabularySearchService extends MeilisearchService implements VocabularySearchService {

    public MeilisearchVocabularySearchService(MeilisearchProperties properties) {
        super(properties);
    }

    public void clearAll() {
        clearAllByPrefix("");
    }

    public boolean isIndexExists(String indexUid) {
        try {
            client.getIndex(indexUid);
            return true;
        } catch (MeilisearchApiException e) {
            if (e.getCode().equals("index_not_found")) {
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    public void saveVocabulary(Vocabulary saved) {
        createIndex(saved.getUid(), "uid");
    }
}
