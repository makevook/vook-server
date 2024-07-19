package vook.server.api.infra.search.vocabulary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.MultiSearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchApiException;
import com.meilisearch.sdk.model.MultiSearchResult;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vook.server.api.domain.vocabulary.logic.TermLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.infra.search.common.MeilisearchProperties;
import vook.server.api.infra.search.common.MeilisearchService;
import vook.server.api.web.term.usecase.SearchTermUseCase;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.meilisearch.sdk.IndexSearchRequest.IndexSearchRequestBuilder;
import static com.meilisearch.sdk.IndexSearchRequest.builder;

@Service
public class MeilisearchVocabularySearchService
        extends
        MeilisearchService
        implements
        VocabularyLogic.SearchManagementService,
        TermLogic.SearchManagementService,
        SearchTermUseCase.SearchService {

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
    public void save(Vocabulary saved) {
        createIndex(saved.getUid(), "uid");
    }

    @Override
    public void delete(Vocabulary vocabulary) {
        TaskInfo taskInfo = client.deleteIndex(vocabulary.getUid());
        client.waitForTask(taskInfo.getTaskUid());
    }

    @Override
    public void save(Term term) {
        saveOrReplaceTerm(term);
    }

    @Override
    public void update(Term term) {
        saveOrReplaceTerm(term);
    }

    @Override
    public void delete(Term term) {
        Index index = client.index(term.getVocabulary().getUid());
        TaskInfo taskInfo = index.deleteDocument(term.getUid());
        client.waitForTask(taskInfo.getTaskUid());
    }

    private void saveOrReplaceTerm(Term term) {
        Index index = client.index(term.getVocabulary().getUid());
        TaskInfo taskInfo = index.addDocuments(getDocument(term));
        client.waitForTask(taskInfo.getTaskUid());
    }

    private String getDocument(Term term) {
        return toJsonString(Document.from(term));
    }

    @Override
    public void saveAll(List<Term> terms) {
        if (terms.isEmpty()) {
            return;
        }
        Index index = client.index(terms.getFirst().getVocabulary().getUid());
        TaskInfo taskInfo = index.addDocuments(getDocuments(terms));
        client.waitForTask(taskInfo.getTaskUid());
    }

    @Override
    public void deleteAll(List<Term> terms) {
        if (terms.isEmpty()) {
            return;
        }

        Map<Vocabulary, List<Term>> vocabularyTermMap = terms.stream().collect(Collectors.groupingBy(Term::getVocabulary));
        vocabularyTermMap.forEach((vocabulary, vocabularyTerms) -> {
            Index index = client.index(vocabulary.getUid());
            TaskInfo taskInfo = index.deleteDocuments(terms.stream().map(Term::getUid).toList());
            client.waitForTask(taskInfo.getTaskUid());
        });
    }

    private String getDocuments(List<Term> terms) {
        return toJsonString(Document.from(terms));
    }

    private String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result search(Params params) {
        MultiSearchRequest request = new RequestBuilder(params).buildMultiSearchRequest();
        Results<MultiSearchResult> results = this.client.multiSearch(request);
        return new ResultBuilder(results).build();
    }

    private record RequestBuilder(Params params) {

        private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
        private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

        public MultiSearchRequest buildMultiSearchRequest() {
            IndexSearchRequestBuilder builder = builder();
            if (params.withFormat()) {
                builder.attributesToHighlight(new String[]{"*"});
                builder.highlightPreTag(StringUtils.hasText(params.highlightPreTag()) ? params.highlightPreTag() : DEFAULT_HIGHLIGHT_PRE_TAG);
                builder.highlightPostTag(StringUtils.hasText(params.highlightPostTag()) ? params.highlightPostTag() : DEFAULT_HIGHLIGHT_POST_TAG);
            }
            builder.limit(Integer.MAX_VALUE);

            MultiSearchRequest request = new MultiSearchRequest();
            params.vocabularyUids().forEach(uid -> {
                params.queries().forEach(query -> {
                    request.addQuery(builder.indexUid(uid).q(query).build());
                });
            });
            return request;
        }
    }

    private record ResultBuilder(
            Results<MultiSearchResult> results
    ) {
        public Result build() {
            return new Result(
                    Arrays.stream(results.getResults())
                            .map(result -> new Result.Record(result.getIndexUid(), result.getQuery(), result.getHits()))
                            .toList()
            );
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

        public static List<Document> from(List<Term> terms) {
            return terms.stream()
                    .map(Document::from)
                    .toList();
        }

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
