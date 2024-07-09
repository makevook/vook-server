package vook.server.api.web.term.reqres;

import lombok.Builder;
import vook.server.api.web.term.usecase.SearchTermUseCase;

import java.util.List;

@Builder
public record TermSearchResponse(
        String query,
        List<Record> records
) {
    public static TermSearchResponse from(SearchTermUseCase.Result result) {
        return TermSearchResponse.builder()
                .query(result.query())
                .records(Record.from(result.records()))
                .build();
    }

    public record Record(
            String vocabularyUid,
            List<Term> hits
    ) {

        public static List<Record> from(List<SearchTermUseCase.Result.Record> input) {
            return input.stream()
                    .map(input1 -> new Record(input1.vocabularyUid(), Term.from(input1.hits())))
                    .toList();
        }
    }

    @Builder
    public record Term(
            String uid,
            String term,
            String meaning,
            String synonyms
    ) {
        private static List<Term> from(List<SearchTermUseCase.Result.Term> terms) {
            return terms.stream()
                    .map(Term::from)
                    .toList();
        }

        private static Term from(SearchTermUseCase.Result.Term term) {
            return Term.builder()
                    .uid(term.uid())
                    .term(term.term())
                    .meaning(term.meaning())
                    .synonyms(term.synonyms())
                    .build();
        }
    }
}
