package vook.server.api.infra.search.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import vook.server.api.domain.demo.logic.DemoTermSearchCommand;
import vook.server.api.domain.demo.logic.DemoTermSearchResult;
import vook.server.api.domain.demo.model.DemoTerm;
import vook.server.api.domain.demo.model.DemoTermSynonym;
import vook.server.api.domain.demo.service.DemoTermSearchService;
import vook.server.api.infra.search.common.MeilisearchProperties;
import vook.server.api.infra.search.common.MeilisearchService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeilisearchDemoTermSearchService extends MeilisearchService implements DemoTermSearchService {

    private static final String DEMO_TERMS_INDEX_UID = "demo-terms";

    private final ObjectMapper objectMapper;

    public MeilisearchDemoTermSearchService(MeilisearchProperties properties, ObjectMapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
    }

    public void clearAll() {
        clearAllByPrefix(DEMO_TERMS_INDEX_UID);
    }

    public void init() {
        createIndex(DEMO_TERMS_INDEX_UID, "id");
    }

    public void addTerms(List<DemoTerm> terms) {
        Index index = client.index(DEMO_TERMS_INDEX_UID);
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    public DemoTermSearchResult search(DemoTermSearchCommand params) {
        SearchRequest searchRequest = params.buildSearchRequest();
        Searchable search = this.client.getIndex(DEMO_TERMS_INDEX_UID).search(searchRequest);
        return DemoTermSearchResult.from(search);
    }

    private String getDocuments(List<DemoTerm> terms) {
        try {
            return objectMapper.writeValueAsString(Document.from(terms));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Document {
        private Long id;
        private String term;
        private String synonyms;
        private String meaning;
        private String createdAt;

        public static List<Document> from(List<DemoTerm> terms) {
            return terms.stream()
                    .map(w -> new Document(
                            w.getId(),
                            w.getTerm(),
                            w.getSynonyms().stream().map(DemoTermSynonym::getSynonym).collect(Collectors.joining("\n")),
                            w.getMeaning(),
                            w.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    ))
                    .toList();
        }
    }
}
