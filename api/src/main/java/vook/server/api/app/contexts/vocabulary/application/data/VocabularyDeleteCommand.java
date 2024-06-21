package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VocabularyDeleteCommand {

    @NotBlank
    private String vocabularyUid;

    public static VocabularyDeleteCommand of(String vocabularyUid) {
        VocabularyDeleteCommand command = new VocabularyDeleteCommand();
        command.vocabularyUid = vocabularyUid;
        return command;
    }
}
