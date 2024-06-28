package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Builder
public record TermCreateCommand(
        @NotNull
        String vocabularyUid,

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
    public Term toEntity(Function<String, Optional<Vocabulary>> vocabularySupplier) {
        return Term.forCreateOf(
                term,
                meaning,
                synonyms,
                vocabularySupplier.apply(vocabularyUid).orElseThrow(VocabularyNotFoundException::new)
        );
    }
}
