package vook.server.api.app.vocabulary.data;

import lombok.Getter;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;

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
