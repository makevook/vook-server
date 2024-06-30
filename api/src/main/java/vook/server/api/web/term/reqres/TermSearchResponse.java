package vook.server.api.web.term.reqres;

import lombok.Builder;
import vook.server.api.usecases.term.SearchTermUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public record TermSearchResponse(
        String query,
        Map<String, List<Term>> hits
) {
    public static TermSearchResponse from(SearchTermUseCase.Result result) {
        return TermSearchResponse.builder()
                .query(result.query())
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
        public static Map<String, List<Term>> from(Map<String, List<SearchTermUseCase.Result.Term>> hits) {
            Map<String, List<Term>> result = new HashMap<>();
            for (Map.Entry<String, List<SearchTermUseCase.Result.Term>> e : hits.entrySet()) {
                result.put(e.getKey(), Term.from(e.getValue()));
            }
            return result;
        }

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
