package vook.server.api.web.vocabulary.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.policy.VocabularyPolicy;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteVocabularyUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;
    private final VocabularyPolicy vocabularyPolicy;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Vocabulary vocabulary = vocabularyLogic.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(command.userUid(), vocabulary);

        vocabularyLogic.delete(command.vocabularyUid());
    }

    public record Command(
            String userUid,
            String vocabularyUid
    ) {
    }
}
