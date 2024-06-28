package vook.server.api.usecases.term;

import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Vocabulary vocabulary = vocabularyService.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(user, vocabulary);

        TermSearchResult result = termSearchService.search(command.toSearchParams());
        return Result.from(result);
    }

    @Builder
    public record Command(
            @NotBlank
            String userUid,
            @NotBlank
            String vocabularyUid,
            @NotBlank
            String query,
            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        public SearchParams toSearchParams() {
            return SearchParams.builder()
                    .vocabularyUid(vocabularyUid)
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
            int processingTimeMs,
            List<Term> hits
    ) {
        public static Result from(TermSearchResult result) {
            return Result.builder()
                    .query(result.query())
                    .processingTimeMs(result.processingTimeMs())
                    .hits(Term.from(result.hits()))
                    .build();
        }

        @Builder
        public record Term(
                String uid,
                String term,
                String meaning,
                String synonyms
        ) {
            public static List<Term> from(ArrayList<HashMap<String, Object>> hits) {
                return hits.stream().map(Term::from).toList();
            }

            public static Term from(HashMap<String, Object> hit) {
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
        TermSearchResult search(SearchParams searchParams);
    }

    @Builder
    public record SearchParams(
            String vocabularyUid,
            String query,
            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
        private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

        public SearchRequest buildSearchRequest() {
            SearchRequest.SearchRequestBuilder builder = SearchRequest.builder();
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
    public record TermSearchResult(
            String query,
            int processingTimeMs,
            ArrayList<HashMap<String, Object>> hits
    ) {

        public static TermSearchResult from(Searchable search) {
            return TermSearchResult.builder()
                    .query(search.getQuery())
                    .processingTimeMs(search.getProcessingTimeMs())
                    .hits(search.getHits())
                    .build();
        }
    }
}
