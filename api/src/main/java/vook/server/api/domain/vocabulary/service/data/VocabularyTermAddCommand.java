package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import vook.server.api.domain.vocabulary.model.TermId;

@Getter
public class VocabularyTermAddCommand {

    @NotBlank
    private String vocabularyUid;

    @NotNull
    private TermId termId;

    @Builder
    public static VocabularyTermAddCommand of(String vocabularyUid, TermId termId) {
        VocabularyTermAddCommand command = new VocabularyTermAddCommand();
        command.vocabularyUid = vocabularyUid;
        command.termId = termId;
        return command;
    }
}
