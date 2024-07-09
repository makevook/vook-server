package vook.server.api.domain.vocabulary.logic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record VocabularyUpdateCommand(
        @NotBlank
        String vocabularyUid,

        @NotBlank
        @Size(min = 1, max = 20)
        String name
) {
}
