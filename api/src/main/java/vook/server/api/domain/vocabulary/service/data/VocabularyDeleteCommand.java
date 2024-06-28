package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record VocabularyDeleteCommand(
        @NotBlank
        String vocabularyUid
) {
}
