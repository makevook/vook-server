package vook.server.api.web.term.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.TermLogic;
import vook.server.api.domain.vocabulary.logic.dto.TermUpdateCommand;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class UpdateTermUseCase {

    private final UserLogic userLogic;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermLogic termLogic;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Term term = termLogic.getByUid(command.termUid());
        vocabularyPolicy.validateOwner(command.userUid(), term.getVocabulary());

        termLogic.update(command.toServiceCommand());
    }

    public record Command(
            String userUid,
            String termUid,
            String term,
            String meaning,
            List<String> synonyms
    ) {
        public TermUpdateCommand toServiceCommand() {
            return TermUpdateCommand.builder()
                    .uid(termUid)
                    .term(term)
                    .meaning(meaning)
                    .synonyms(synonyms)
                    .build();
        }
    }
}
