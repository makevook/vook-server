package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import vook.server.api.domain.vocabulary.model.UserId;

@Builder
public record VocabularyCreateCommand(
        @NotBlank
        @Size(min = 1, max = 20)
        String name,

        @NotNull
        UserId userId
) {
}
