package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vook.server.api.app.contexts.vocabulary.domain.UserId;

@Getter
public class VocabularyDeleteCommand {

    @NotBlank
    private String vocabularyUid;

    @NotNull
    private UserId userId;

    public static VocabularyDeleteCommand of(String vocabularyUid, UserId userId) {
        VocabularyDeleteCommand command = new VocabularyDeleteCommand();
        command.vocabularyUid = vocabularyUid;
        command.userId = userId;
        return command;
    }
}
