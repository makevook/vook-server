package vook.server.api.usecases.term;

import com.meilisearch.sdk.IndexSearchRequest;
import com.meilisearch.sdk.MultiSearchRequest;
import com.meilisearch.sdk.model.MultiSearchResult;
import com.meilisearch.sdk.model.Results;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SearchTermUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermSearchService termSearchService;

    public Result execute(@Valid Command command) {
        User user = userService.getCompletedUserByUid(command.userUid());
        List<Vocabulary> userVocabularies = vocabularyService.findAllBy(new UserId(user.getId()));
        vocabularyPolicy.validateOwner(userVocabularies, command.vocabularyUids());

        SearchResult result = termSearchService.search(command.toSearchParams());
        return Result.from(command.query(), result);
    }

    @Builder
    public record Command(
            @NotBlank
            String userUid,
            @NotEmpty
            List<String> vocabularyUids,
            @NotBlank
            String query,
            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        public SearchParams toSearchParams() {
            return SearchParams.builder()
                    .vocabularyUid(vocabularyUids)
                    .query(query)
                    .withFormat(withFormat)
                    .highlightPreTag(highlightPreTag)
                    .highlightPostTag(highlightPostTag)
                    .build();
        }
    }

    @Builder
    public record Result(
            String query,
            Map<String, List<Term>> hits
    ) {
        public static Result from(String query, SearchResult result) {
            return Result.builder()
                    .query(query)
                    .hits(Term.from(result.results()))
                    .build();
        }

        @Builder
        public record Term(
                String uid,
                String term,
                String meaning,
                String synonyms
        ) {
            public static Map<String, List<Term>> from(List<MultiSearchResult> results) {
                return results.stream()
                        .map(Term::from)
                        .reduce(new HashMap<>(), (identify, vocabularyTermMap) -> {
                            identify.putAll(vocabularyTermMap);
                            return identify;
                        });
            }

            private static Map<String, List<Term>> from(MultiSearchResult result) {
                return Map.of(
                        result.getIndexUid(),
                        result.getHits().stream()
                                .map(hit -> {
                                    Object formatted = hit.get("_formatted");
                                    if (formatted instanceof Map formattedDocument) {
                                        return formattedDocument;
                                    } else {
                                        return hit;
                                    }
                                })
                                .map(Term::from)
                                .toList()
                );
            }

            public static Term from(Map<String, Object> hit) {
                return Term.builder()
                        .uid((String) hit.get("uid"))
                        .term((String) hit.get("term"))
                        .meaning((String) hit.get("meaning"))
                        .synonyms((String) hit.get("synonyms"))
                        .build();
            }
        }
    }

    public interface TermSearchService {
        SearchResult search(SearchParams searchParams);
    }

    @Builder
    public record SearchParams(
            List<String> vocabularyUid,
            String query,
            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
        private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

        public MultiSearchRequest buildMultiSearchRequest() {
            MultiSearchRequest request = new MultiSearchRequest();
            vocabularyUid.forEach(uid -> request.addQuery(buildIndexSearchRequest(uid)));
            return request;
        }

        private IndexSearchRequest buildIndexSearchRequest(String uid) {
            IndexSearchRequest.IndexSearchRequestBuilder builder = IndexSearchRequest.builder();
            builder.indexUid(uid);
            if (withFormat) {
                builder.attributesToHighlight(new String[]{"*"});
                builder.highlightPreTag(StringUtils.hasText(highlightPreTag) ? highlightPreTag : DEFAULT_HIGHLIGHT_PRE_TAG);
                builder.highlightPostTag(StringUtils.hasText(highlightPostTag) ? highlightPostTag : DEFAULT_HIGHLIGHT_POST_TAG);
            }

            return builder
                    .q(query)
                    .limit(Integer.MAX_VALUE)
                    .build();
        }
    }

    @Builder
    public record SearchResult(
            List<MultiSearchResult> results
    ) {
        public static SearchResult from(Results<MultiSearchResult> results) {
            return SearchResult.builder()
                    .results(Arrays.stream(results.getResults()).toList())
                    .build();
        }
    }
}
