package vook.server.api.web.term.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import vook.server.api.web.term.usecase.RetrieveTermUseCase;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TermResponse(
        String termUid,
        String term,
        String meaning,
        List<String> synonyms,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdAt
) {
    public static List<TermResponse> from(RetrieveTermUseCase.Result result) {
        return result.terms().stream().map(TermResponse::from).toList();
    }

    public static TermResponse from(RetrieveTermUseCase.Result.Tuple term) {
        return TermResponse.builder()
                .termUid(term.termUid())
                .term(term.term())
                .meaning(term.meaning())
                .synonyms(term.synonyms())
                .createdAt(term.createdAt())
                .build();
    }
}
