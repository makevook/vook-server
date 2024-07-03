package vook.server.api.usecases.common.polices;

import org.springframework.stereotype.Component;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.usecases.common.exception.UseCaseException;

import java.util.List;

@Component
public class VocabularyPolicy {

    public void validateOwner(String userUid, Vocabulary vocabulary) throws NotValidVocabularyOwnerException {
        if (!vocabulary.isValidOwner(new UserUid(userUid))) {
            throw new NotValidVocabularyOwnerException();
        }
    }

    public void validateOwner(List<Vocabulary> userVocabularies, List<String> vocabularyUids) {
        if (userVocabularies.stream().noneMatch(v -> vocabularyUids.contains(v.getUid()))) {
            throw new NotValidVocabularyOwnerException();
        }
    }

    public static class NotValidVocabularyOwnerException extends UseCaseException {
    }
}
