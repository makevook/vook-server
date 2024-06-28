package vook.server.api.infra.search.vocabulary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.TermSearchService;
import vook.server.api.domain.vocabulary.service.VocabularySearchService;
import vook.server.api.infra.search.common.MeilisearchProperties;
import vook.server.api.infra.search.common.MeilisearchService;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class MeilisearchVocabularySearchService extends MeilisearchService implements VocabularySearchService, TermSearchService {

    private final ObjectMapper objectMapper;

    public MeilisearchVocabularySearchService(MeilisearchProperties properties, ObjectMapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
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

    public boolean isDocumentExists(String indexUid, String documentUid) {
        try {
            client.getIndex(indexUid).getRawDocument(documentUid);
            return true;
        } catch (MeilisearchApiException e) {
            if (e.getCode().equals("document_not_found")) {
                return false;
            } else {
                throw e;
            }
        }
    }

    public Map getDocument(String indexUid, String documentUid) {
        return client.getIndex(indexUid).getDocument(documentUid, Map.class);
    }

    @Override
    public void saveVocabulary(Vocabulary saved) {
        createIndex(saved.getUid(), "uid");
    }

    @Override
    public void deleteVocabulary(Vocabulary vocabulary) {
        TaskInfo taskInfo = client.deleteIndex(vocabulary.getUid());
        client.waitForTask(taskInfo.getTaskUid());
    }

    @Override
    public void saveTerm(Term term) {
        saveOrReplaceTerm(term);
    }

    @Override
    public void update(Term term) {
        saveOrReplaceTerm(term);
    }

    private void saveOrReplaceTerm(Term term) {
        Index index = client.index(term.getVocabulary().getUid());
        TaskInfo taskInfo = index.addDocuments(getDocument(term));
        client.waitForTask(taskInfo.getTaskUid());
    }

    private String getDocument(Term term) {
        try {
            return objectMapper.writeValueAsString(Document.from(term));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Document {
        private String uid;
        private String term;
        private String synonyms;
        private String meaning;
        private String createdAt;

        public static Document from(Term term) {
            return new Document(
                    term.getUid(),
                    term.getTerm(),
                    String.join(",", term.getSynonyms()),
                    term.getMeaning(),
                    term.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
            );
        }
    }
}
