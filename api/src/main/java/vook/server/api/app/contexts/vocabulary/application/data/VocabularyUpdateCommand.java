package vook.server.api.app.contexts.vocabulary.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VocabularyUpdateCommand {

    @NotBlank
    private String vocabularyUid;

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;

    public static VocabularyUpdateCommand of(String vocabularyUid, String name) {
        VocabularyUpdateCommand command = new VocabularyUpdateCommand();
        command.vocabularyUid = vocabularyUid;
        command.name = name;
        return command;
    }
}
