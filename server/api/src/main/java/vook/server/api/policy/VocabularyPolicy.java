package vook.server.api.policy;

import org.springframework.stereotype.Component;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;

import java.util.List;

@Component
public class VocabularyPolicy {

    public void validateOwner(String userUid, Vocabulary vocabulary) throws NotValidVocabularyOwnerException {
        if (!vocabulary.isValidOwner(new UserUid(userUid))) {
            throw new NotValidVocabularyOwnerException();
        }
    }

    public void validateOwner(List<String> userVocabularyUids, List<String> targetVocabularyUids) {
        targetVocabularyUids.forEach(uid -> {
            if (!userVocabularyUids.contains(uid)) {
                throw new NotValidVocabularyOwnerException();
            }
        });
    }

    public static class NotValidVocabularyOwnerException extends PolicyException {
    }
}
