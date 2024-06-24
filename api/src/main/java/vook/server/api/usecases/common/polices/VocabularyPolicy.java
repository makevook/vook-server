package vook.server.api.usecases.common.polices;

import org.springframework.stereotype.Component;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.usecases.common.exception.UseCaseException;

@Component
public class VocabularyPolicy {

    public void validateOwner(User user, Vocabulary vocabulary) {
        if (!vocabulary.isValidOwner(new UserId(user.getId()))) {
            throw new NotValidVocabularyOwnerException();
        }
    }

    public static class NotValidVocabularyOwnerException extends UseCaseException {
    }
}
