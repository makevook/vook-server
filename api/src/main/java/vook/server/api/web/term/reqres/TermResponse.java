package vook.server.api.web.term.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vook.server.api.usecases.term.RetrieveTermUseCase;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TermResponse {

    String termUid;
    String term;
    String meaning;
    List<String> synonyms;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

    public static List<TermResponse> from(RetrieveTermUseCase.Result result) {
        return result.terms().stream().map(TermResponse::from).toList();
    }

    public static TermResponse from(RetrieveTermUseCase.Result.Tuple term) {
        TermResponse result = new TermResponse();
        result.termUid = term.termUid();
        result.term = term.term();
        result.meaning = term.meaning();
        result.synonyms = term.synonyms();
        result.createdAt = term.createdAt();
        return result;
    }
}
