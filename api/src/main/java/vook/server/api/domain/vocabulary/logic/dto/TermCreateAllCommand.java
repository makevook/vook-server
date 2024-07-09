package vook.server.api.domain.vocabulary.logic.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;

@Builder
public record TermCreateAllCommand(
        @NotNull
        String vocabularyUid,

        @Valid
        @NotEmpty
        List<TermInfo> termInfos
) {

    @Builder
    public record TermInfo(
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

        public Term toEntity(Vocabulary vocabulary) {
            return Term.forCreateOf(term, meaning, synonyms, vocabulary);
        }
    }
}
