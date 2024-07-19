package vook.server.api.web.vocabulary.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VocabularyCreateRequest(
        @NotBlank
        @Size(min = 1, max = 20)
        String name
) {
}
