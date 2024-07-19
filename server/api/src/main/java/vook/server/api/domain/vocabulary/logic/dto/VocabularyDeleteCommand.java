package vook.server.api.domain.vocabulary.logic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record VocabularyDeleteCommand(
        @NotBlank
        String vocabularyUid
) {
}
