package vook.server.api.app.polices;

import org.springframework.stereotype.Component;
import vook.server.api.app.common.exception.AppException;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.UserId;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;

@Component
public class VocabularyPolicy {

    public void validateOwner(User user, Vocabulary vocabulary) {
        if (!vocabulary.isValidOwner(new UserId(user.getId()))) {
            throw new NotValidOwnerException();
        }
    }

    public static class NotValidOwnerException extends AppException {
    }
}
