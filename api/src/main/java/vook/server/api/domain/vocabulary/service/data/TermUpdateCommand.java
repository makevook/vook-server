package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import vook.server.api.domain.vocabulary.model.Term;

import java.util.List;

@Builder
public record TermUpdateCommand(
        @NotNull
        String uid,

        @NotBlank
        @Size(min = 1, max = 100)
        String term,

        @NotBlank
        @Size(min = 1, max = 2000)
        String meaning,

        @NotNull
        @Valid
        @Size(max = 10)
        List<@Size(min = 1, max = 100) String> synonyms
) {

    public Term toEntity() {
        return Term.forUpdateOf(term, meaning, synonyms);
    }
}
