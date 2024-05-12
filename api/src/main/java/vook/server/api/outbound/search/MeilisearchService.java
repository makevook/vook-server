package vook.server.api.outbound.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Searchable;
import com.meilisearch.sdk.model.TaskInfo;
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
        TaskInfo index = client.createIndex(getIndexUid(glossary), "id");
        client.waitForTask(index.getTaskUid());
    }

    @Override
    public void addTerms(List<Term> terms, Glossary glossary) {
        Index index = client.index(getIndexUid(glossary));
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    @Override
    public SearchResult search(SearchParams params) {
        SearchRequest searchRequest = SearchRequest.builder()
                .q(params.getQuery())
                .attributesToHighlight(new String[]{"*"})
                .highlightPreTag("<strong>")
                .highlightPostTag("</strong>")
                .build();
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

        public static List<Document> from(List<Term> terms) {
            return terms.stream()
                    .map(w -> new Document(
                            w.getId(),
                            w.getTerm(),
                            w.getSynonyms().stream().map(TermSynonym::getSynonym).collect(Collectors.joining("\n")),
                            w.getMeaning()
                    ))
                    .toList();
        }
    }
}
