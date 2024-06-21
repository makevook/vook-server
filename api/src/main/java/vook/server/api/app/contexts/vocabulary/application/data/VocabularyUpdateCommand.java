package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import vook.server.api.app.contexts.user.domain.User;

@Getter
public class VocabularyUpdateCommand {

    @NotBlank
    private String vocabularyUid;

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private User user;

    public static VocabularyUpdateCommand of(String vocabularyUid, String name, User user) {
        VocabularyUpdateCommand command = new VocabularyUpdateCommand();
        command.vocabularyUid = vocabularyUid;
        command.name = name;
        command.user = user;
        return command;
    }
}
