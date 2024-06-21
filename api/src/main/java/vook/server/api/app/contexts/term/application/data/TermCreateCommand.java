package vook.server.api.app.contexts.term.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;

import java.util.List;

@Getter
public class TermCreateCommand {

    @NotNull
    private Vocabulary vocabulary;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    @NotNull
    private List<String> synonyms;

    @Builder
    private static TermCreateCommand of(Vocabulary vocabulary, String term, String meaning, List<String> synonyms) {
        TermCreateCommand command = new TermCreateCommand();
        command.vocabulary = vocabulary;
        command.term = term;
        command.meaning = meaning;
        command.synonyms = synonyms;
        return command;
    }

    public Term toEntity() {
        return Term.forCreateOf(term, meaning, vocabulary);
    }
}
