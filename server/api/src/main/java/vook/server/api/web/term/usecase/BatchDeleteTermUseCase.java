package vook.server.api.web.term.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.term.TermLogic;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class BatchDeleteTermUseCase {

    private final UserLogic userLogic;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermLogic termLogic;

    public void execute(Command command) {
        validate(command.userUid(), command.termUids());
        termLogic.batchDelete(command.termUids());
    }

    private void validate(String userUid, List<String> termUids) {
        userLogic.validateCompletedUserByUid(userUid);
        termUids.forEach(termUid -> {
            Term term = termLogic.getByUid(termUid);
            vocabularyPolicy.validateOwner(userUid, term.getVocabulary());
        });
    }

    public record Command(
            String userUid,
            List<String> termUids
    ) {
    }
}
