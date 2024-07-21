package vook.server.api.web.term.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.term.TermLogic;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

@UseCase
@RequiredArgsConstructor
public class DeleteTermUseCase {

    private final UserLogic userLogic;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermLogic termLogic;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Term term = termLogic.getByUid(command.termUid());
        vocabularyPolicy.validateOwner(command.userUid(), term.getVocabulary());

        termLogic.delete(command.termUid());
    }

    public record Command(
            String userUid,
            String termUid
    ) {
    }
}
