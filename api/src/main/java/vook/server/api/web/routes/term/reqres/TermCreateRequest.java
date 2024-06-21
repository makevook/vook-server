package vook.server.api.web.routes.term.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vook.server.api.app.domain.term.data.TermCreateCommand;
import vook.server.api.model.vocabulary.Vocabulary;

import java.util.ArrayList;
import java.util.List;

@Data
public class TermCreateRequest {

    @NotBlank
    private String vocabularyUid;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    private List<String> synonyms = new ArrayList<>();

    public TermCreateCommand toCommand(Vocabulary vocabulary) {
        return TermCreateCommand.of(vocabulary, term, meaning, synonyms);
    }
}
