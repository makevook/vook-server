package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import vook.server.api.app.contexts.vocabulary.domain.UserId;

@Getter
public class VocabularyCreateCommand {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private UserId userId;

    public static VocabularyCreateCommand of(String name, UserId userId) {
        VocabularyCreateCommand command = new VocabularyCreateCommand();
        command.name = name;
        command.userId = userId;
        return command;
    }
}
