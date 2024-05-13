package vook.server.api.outbound.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TypoTolerance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.demo.DemoGlossary;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.model.demo.DemoTermSynonym;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemoTermSearchService extends MeilisearchService {

    private static final String INDEX_PREFIX = "demo-";

    private final ObjectMapper objectMapper;

    public void clearAll() {
        clearAll(INDEX_PREFIX);
    }

    public void createGlossary(DemoGlossary demoGlossary) {
        String indexUid = getIndexUid(demoGlossary);

        TaskInfo indexCreateTask = client.createIndex(indexUid, "id");
        client.waitForTask(indexCreateTask.getTaskUid());

        // 용어, 동의어, 뜻에 대해서만 검색
        client.index(indexUid).updateSearchableAttributesSettings(new String[]{
                "term",
                "synonyms",
                "meaning"
        });

        // 오타 용인을 비활성화 하여도 띄어쓰기에 대해서는 검색이 됨으로 비활성화 함
        TypoTolerance typoTolerance = new TypoTolerance();
        typoTolerance.setEnabled(false);
        client.index(indexUid).updateTypoToleranceSettings(typoTolerance);
    }

    public void addTerms(DemoGlossary demoGlossary, List<DemoTerm> terms) {
        String indexUid = getIndexUid(demoGlossary);
        Index index = client.index(indexUid);
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    public DemoTermSearchResult search(DemoTermSearchParams params) {
        SearchRequest searchRequest = params.buildSearchRequest();
        Searchable search = this.client.getIndex(getIndexUid(params.getDemoGlossary())).search(searchRequest);
        return DemoTermSearchResult.from(search);
    }

    private static String getIndexUid(DemoGlossary glossary) {
        return INDEX_PREFIX + glossary.getUid();
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
