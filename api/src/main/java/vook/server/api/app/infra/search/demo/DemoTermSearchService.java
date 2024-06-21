package vook.server.api.app.infra.search.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TypoTolerance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import vook.server.api.app.contexts.demo.domain.DemoTerm;
import vook.server.api.app.contexts.demo.domain.DemoTermSynonym;
import vook.server.api.app.infra.search.common.MeilisearchProperties;
import vook.server.api.app.infra.search.common.MeilisearchService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemoTermSearchService extends MeilisearchService {

    private static final String DEMO_TERMS_INDEX_UID = "demo-terms";

    private final ObjectMapper objectMapper;

    public DemoTermSearchService(MeilisearchProperties properties, ObjectMapper objectMapper) {
        super(properties);
        this.objectMapper = objectMapper;
    }

    public void clearAll() {
        clearAll(DEMO_TERMS_INDEX_UID);
    }

    public void init() {
        TaskInfo indexCreateTask = client.createIndex(DEMO_TERMS_INDEX_UID, "id");
        client.waitForTask(indexCreateTask.getTaskUid());

        // 용어, 동의어, 뜻에 대해서만 검색
        TaskInfo updateSearchableTask = client.index(DEMO_TERMS_INDEX_UID).updateSearchableAttributesSettings(new String[]{
                "term",
                "synonyms",
                "meaning"
        });
        client.waitForTask(updateSearchableTask.getTaskUid());

        // 용어, 동의어, 뜻, 생성일시에 대해 정렬 가능
        TaskInfo updateSortableTask = client.index(DEMO_TERMS_INDEX_UID).updateSortableAttributesSettings(new String[]{
                "term",
                "synonyms",
                "meaning",
                "createdAt"
        });
        client.waitForTask(updateSortableTask.getTaskUid());

        client.index(DEMO_TERMS_INDEX_UID).updateRankingRulesSettings(new String[]{
                "sort",
                "words",
                "typo",
                "proximity",
                "attribute",
                "exactness"
        });

        // 오타 용인을 비활성화 하여도 띄어쓰기에 대해서는 검색이 됨으로 비활성화 함
        TypoTolerance typoTolerance = new TypoTolerance();
        typoTolerance.setEnabled(false);
        TaskInfo updateTypoTask = client.index(DEMO_TERMS_INDEX_UID).updateTypoToleranceSettings(typoTolerance);
        client.waitForTask(updateTypoTask.getTaskUid());
    }

    public void addTerms(List<DemoTerm> terms) {
        Index index = client.index(DEMO_TERMS_INDEX_UID);
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    public DemoTermSearchResult search(DemoTermSearchParams params) {
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
