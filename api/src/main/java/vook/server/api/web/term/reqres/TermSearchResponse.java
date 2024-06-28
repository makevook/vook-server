package vook.server.api.web.term.reqres;

import lombok.Builder;
import vook.server.api.usecases.term.SearchTermUseCase;

import java.util.List;

@Builder
public record TermSearchResponse(
        String query,
        List<Term> hits
) {
    public static TermSearchResponse from(SearchTermUseCase.Result result) {
        return TermSearchResponse.builder()
                .query(result.query())
                .hits(Term.from(result.hits()))
                .build();
    }

    @Builder
    public record Term(
            String vocabularyUid,
            String uid,
            String term,
            String meaning,
            String synonyms
    ) {
        public static List<Term> from(List<SearchTermUseCase.Result.Term> hits) {
            return hits.stream().map(Term::from).toList();
        }

        private static Term from(SearchTermUseCase.Result.Term term) {
            return Term.builder()
                    .vocabularyUid(term.vocabularyUid())
                    .uid(term.uid())
                    .term(term.term())
                    .meaning(term.meaning())
                    .synonyms(term.synonyms())
                    .build();
        }
    }
}
