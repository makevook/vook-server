package vook.server.api.app.context.vocabulary.data;

import lombok.Getter;
import vook.server.api.app.context.user.domain.User;
import vook.server.api.app.context.vocabulary.domain.Vocabulary;

@Getter
public class VocabularyCreateCommand {

    private String name;
    private User user;

    public static VocabularyCreateCommand of(String name, User user) {
        VocabularyCreateCommand command = new VocabularyCreateCommand();
        command.name = name;
        command.user = user;
        return command;
    }

    public Vocabulary toVocabulary() {
        return Vocabulary.forCreateOf(name, user);
    }
}
