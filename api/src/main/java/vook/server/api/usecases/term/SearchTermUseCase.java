package vook.server.api.usecases.term;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.ArrayList;
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

        TermSearchService.Result result = termSearchService.search(command.toSearchParams());
        return SearchTermUseCase.Result.from(result);
    }

    @Builder
    public record Command(
            @NotBlank
            String userUid,

            @Valid
            @NotEmpty
            List<@NotBlank String> vocabularyUids,

            @NotBlank
            String query,

            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
        public TermSearchService.Params toSearchParams() {
            return TermSearchService.Params.builder()
                    .vocabularyUids(vocabularyUids)
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
            List<Record> records
    ) {
        public static Result from(TermSearchService.Result input) {
            return Result.builder()
                    .query(input.query())
                    .records(Record.from(input.records()))
                    .build();
        }

        public record Record(
                String vocabularyUid,
                List<Term> hits
        ) {
            public static List<Record> from(List<TermSearchService.Result.Record> input) {
                List<Record> result = new ArrayList<>();
                for (TermSearchService.Result.Record record : input) {
                    result.add(new Record(record.vocabularyUid(), Term.from(record)));
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
            private static List<Term> from(TermSearchService.Result.Record record) {
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

    public interface TermSearchService {
        Result search(Params params);

        @Builder
        record Params(
                List<String> vocabularyUids,
                String query,
                boolean withFormat,
                String highlightPreTag,
                String highlightPostTag
        ) {
        }

        @Builder
        record Result(
                String query,
                List<Record> records
        ) {
            public record Record(
                    String vocabularyUid,
                    ArrayList<HashMap<String, Object>> hits
            ) {
            }
        }
    }
}
