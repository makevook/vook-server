package vook.server.api.app.domain.vocabulary.data;

import lombok.Getter;
import vook.server.api.app.domain.user.model.User;

@Getter
public class VocabularyDeleteCommand {

    private String vocabularyUid;
    private User user;

    public static VocabularyDeleteCommand of(String vocabularyUid, User user) {
        VocabularyDeleteCommand command = new VocabularyDeleteCommand();
        command.vocabularyUid = vocabularyUid;
        command.user = user;
        return command;
    }
}
