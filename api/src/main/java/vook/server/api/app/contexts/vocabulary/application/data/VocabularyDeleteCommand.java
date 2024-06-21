package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vook.server.api.app.contexts.user.domain.User;

@Getter
public class VocabularyDeleteCommand {

    @NotBlank
    private String vocabularyUid;

    @NotNull
    private User user;

    public static VocabularyDeleteCommand of(String vocabularyUid, User user) {
        VocabularyDeleteCommand command = new VocabularyDeleteCommand();
        command.vocabularyUid = vocabularyUid;
        command.user = user;
        return command;
    }
}
