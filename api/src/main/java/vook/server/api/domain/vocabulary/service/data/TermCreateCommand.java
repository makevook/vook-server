package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Getter
public class TermCreateCommand {

    @NotNull
    private String vocabularyUid;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    @NotNull
    private List<String> synonyms;

    @Builder
    private static TermCreateCommand of(String vocabularyUid, String term, String meaning, List<String> synonyms) {
        TermCreateCommand command = new TermCreateCommand();
        command.vocabularyUid = vocabularyUid;
        command.term = term;
        command.meaning = meaning;
        command.synonyms = synonyms;
        return command;
    }

    public Term toEntity(Function<String, Optional<Vocabulary>> vocabularySupplier) {
        return Term.forCreateOf(
                term,
                meaning,
                synonyms,
                vocabularySupplier.apply(vocabularyUid).orElseThrow(VocabularyNotFoundException::new)
        );
    }
}
