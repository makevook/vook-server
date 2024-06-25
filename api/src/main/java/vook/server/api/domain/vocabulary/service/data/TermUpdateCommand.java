package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import vook.server.api.domain.vocabulary.model.Term;

import java.util.List;

@Getter
public class TermUpdateCommand {

    @NotNull
    private String uid;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    @NotNull
    private List<String> synonyms;

    @Builder
    private static TermUpdateCommand of(String uid, String term, String meaning, List<String> synonyms) {
        TermUpdateCommand command = new TermUpdateCommand();
        command.uid = uid;
        command.term = term;
        command.meaning = meaning;
        command.synonyms = synonyms;
        return command;
    }

    public Term toEntity() {
        return Term.forUpdateOf(term, meaning, synonyms);
    }
}
