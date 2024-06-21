package vook.server.api.web.routes.vocabulary.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vook.server.api.app.domain.vocabulary.model.Vocabulary;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class VocabularyResponse {

    private String uid;
    private String name;
    private Integer termCount;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdAt;

    public static List<VocabularyResponse> from(List<Vocabulary> vocabularies) {
        return vocabularies.stream().map(VocabularyResponse::from).toList();
    }

    public static VocabularyResponse from(Vocabulary vocabulary) {
        VocabularyResponse result = new VocabularyResponse();
        result.uid = vocabulary.getUid();
        result.name = vocabulary.getName();
        result.termCount = vocabulary.termCount();
        result.createdAt = vocabulary.getCreatedAt();
        return result;
    }
}
