package vook.server.api.web.term.reqres;

import lombok.Builder;
import vook.server.api.web.term.usecase.SearchTermUseCase;

import java.util.List;

@Builder
public record TermSearchResponse(
        List<Record> records
) {
    public static TermSearchResponse from(SearchTermUseCase.Result result) {
        return TermSearchResponse.builder()
                .records(Record.from(result.records()))
                .build();
    }

    public record Record(
            String vocabularyUid,
            String query,
            List<Term> hits
    ) {

        public static List<Record> from(List<SearchTermUseCase.Result.Record> resultRecords) {
            return resultRecords.stream()
                    .map(resultRecord -> new Record(resultRecord.vocabularyUid(), resultRecord.query(), Term.from(resultRecord.hits())))
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
