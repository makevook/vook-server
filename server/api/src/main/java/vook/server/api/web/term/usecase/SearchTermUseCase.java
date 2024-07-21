package vook.server.api.web.term.usecase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.logic.vocabulary.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UseCase
@RequiredArgsConstructor
public class SearchTermUseCase {

    private final VocabularyLogic vocabularyLogic;
    private final VocabularyPolicy vocabularyPolicy;
    private final SearchService searchService;

    public Result execute(@Valid Command command) {
        List<String> userVocabularyUids = vocabularyLogic.findAllUidsBy(new UserUid(command.userUid()));
        vocabularyPolicy.validateOwner(userVocabularyUids, command.vocabularyUids());

        SearchService.Result result = searchService.search(command.toSearchParams());
        return SearchTermUseCase.Result.from(result);
    }

    @Builder
    public record Command(
            @NotBlank
            String userUid,

            @Valid
            @NotEmpty
            List<@NotBlank String> vocabularyUids,

            @Valid
            @NotEmpty
            List<@NotBlank String> queries,

            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        public SearchService.Params toSearchParams() {
            return SearchService.Params.builder()
                    .vocabularyUids(vocabularyUids)
                    .queries(queries)
                    .withFormat(withFormat)
                    .highlightPreTag(highlightPreTag)
                    .highlightPostTag(highlightPostTag)
                    .build();
        }
    }

    @Builder
    public record Result(
            List<Record> records
    ) {
        public static Result from(SearchService.Result input) {
            return Result.builder()
                    .records(Record.from(input.records()))
                    .build();
        }

        public record Record(
                String vocabularyUid,
                String query,
                List<Term> hits
        ) {
            public static List<Record> from(List<SearchService.Result.Record> input) {
                List<Record> result = new ArrayList<>();
                for (SearchService.Result.Record record : input) {
                    result.add(new Record(record.vocabularyUid(), record.query(), Term.from(record)));
                }
                return result;
            }
        }

        @Builder
        public record Term(
                String uid,
                String term,
                String meaning,
                String synonyms
        ) {
            private static List<Term> from(SearchService.Result.Record record) {
                return record.hits().stream()
                        .map(hit -> {
                            Object formatted = hit.get("_formatted");
                            if (formatted instanceof Map formattedDocument) {
                                return formattedDocument;
                            } else {
                                return hit;
                            }
                        })
                        .map(Term::from)
                        .toList();
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

    public interface SearchService {
        Result search(Params params);

        @Builder
        record Params(
                List<String> vocabularyUids,
                List<String> queries,
                boolean withFormat,
                String highlightPreTag,
                String highlightPostTag
        ) {
        }

        @Builder
        record Result(
                List<Record> records
        ) {
            public record Record(
                    String vocabularyUid,
                    String query,
                    ArrayList<HashMap<String, Object>> hits
            ) {
            }
        }
    }
}
