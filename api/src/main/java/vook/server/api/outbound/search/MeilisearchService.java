package vook.server.api.outbound.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;
import vook.server.api.model.TermSynonym;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeilisearchService implements SearchService, SearchClearable {

    private final ObjectMapper objectMapper;

    @Value("${service.meilisearch.host:}")
    private String host;

    @Value("${service.meilisearch.apiKey:}")
    private String apiKey;

    private Client client;

    @PostConstruct
    public void postConstruct() {
        this.client = new Client(new Config(host, apiKey));
    }

    @Override
    public void clearAll() {
        Results<Index> indexes = client.getIndexes(new IndexesQuery() {{
            setLimit(Integer.MAX_VALUE);
        }});
        Arrays.stream(indexes.getResults()).forEach(index -> {
            client.deleteIndex(index.getUid());
        });
    }

    @Override
    public void createGlossary(Glossary glossary) {
        String indexUid = getIndexUid(glossary);
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

    @Override
    public void addTerms(List<Term> terms, Glossary glossary) {
        Index index = client.index(getIndexUid(glossary));
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    @Override
    public SearchResult search(SearchParams params) {
        SearchRequest searchRequest = params.buildSearchRequest();
        Searchable search = this.client.getIndex(getIndexUid(params.getGlossary())).search(searchRequest);
        return SearchResult.from(search);
    }

    @NotNull
    private static String getIndexUid(Glossary glossary) {
        return glossary.getUid();
    }

    private String getDocuments(List<Term> terms) {
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

        public static List<Document> from(List<Term> terms) {
            return terms.stream()
                    .map(w -> new Document(
                            w.getId(),
                            w.getTerm(),
                            w.getSynonyms().stream().map(TermSynonym::getSynonym).collect(Collectors.joining("\n")),
                            w.getMeaning(),
                            w.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    ))
                    .toList();
        }
    }
}
