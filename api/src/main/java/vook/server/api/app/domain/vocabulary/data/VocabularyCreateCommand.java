package vook.server.api.app.domain.vocabulary.data;

import lombok.Getter;
import vook.server.api.app.domain.user.model.User;
import vook.server.api.app.domain.vocabulary.model.Vocabulary;

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
