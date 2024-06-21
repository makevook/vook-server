package vook.server.api.app.context.vocabulary.data;

import lombok.Getter;
import vook.server.api.app.context.user.model.User;

@Getter
public class VocabularyUpdateCommand {

    private String vocabularyUid;
    private String name;
    private User user;

    public static VocabularyUpdateCommand of(String vocabularyUid, String name, User user) {
        VocabularyUpdateCommand command = new VocabularyUpdateCommand();
        command.vocabularyUid = vocabularyUid;
        command.name = name;
        command.user = user;
        return command;
    }
}
