package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import vook.server.api.domain.vocabulary.model.UserId;

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
