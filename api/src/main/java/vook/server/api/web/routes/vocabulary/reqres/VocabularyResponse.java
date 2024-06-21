package vook.server.api.web.routes.vocabulary.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vook.server.api.app.crosscontext.usecases.vocabulary.RetrieveVocabularyUseCase;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class VocabularyResponse {

    private String uid;
    private String name;
    private Integer termCount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

    public static List<VocabularyResponse> from(RetrieveVocabularyUseCase.Result vocabularies) {
        return vocabularies.vocabularies().stream().map(VocabularyResponse::from).toList();
    }

    public static VocabularyResponse from(RetrieveVocabularyUseCase.Result.Tuple vocabulary) {
        VocabularyResponse result = new VocabularyResponse();
        result.uid = vocabulary.uid();
        result.name = vocabulary.name();
        result.termCount = vocabulary.termCount();
        result.createdAt = vocabulary.createdAt();
        return result;
    }
}
