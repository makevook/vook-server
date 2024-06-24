package vook.server.api.domain.vocabulary.service.data;

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
