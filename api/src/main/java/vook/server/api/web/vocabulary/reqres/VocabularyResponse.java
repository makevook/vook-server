package vook.server.api.web.vocabulary.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import vook.server.api.web.vocabulary.usecase.RetrieveVocabularyUseCase;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record VocabularyResponse(
        String uid,
        String name,
        Integer termCount,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        LocalDateTime createdAt
) {

    public static List<VocabularyResponse> from(RetrieveVocabularyUseCase.Result vocabularies) {
        return vocabularies.vocabularies().stream().map(VocabularyResponse::from).toList();
    }

    public static VocabularyResponse from(RetrieveVocabularyUseCase.Result.Tuple vocabulary) {
        return VocabularyResponse.builder()
                .uid(vocabulary.uid())
                .name(vocabulary.name())
                .termCount(vocabulary.termCount())
                .createdAt(vocabulary.createdAt())
                .build();
    }
}
