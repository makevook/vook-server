package vook.server.api.app.context.vocabulary.data;

import lombok.Getter;
import vook.server.api.app.context.user.domain.User;

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
