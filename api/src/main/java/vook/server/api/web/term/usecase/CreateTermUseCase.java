package vook.server.api.web.term.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.TermLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.TermCreateCommand;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class CreateTermUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;
    private final TermLogic termLogic;
    private final VocabularyPolicy vocabularyPolicy;

    public Result execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Vocabulary vocabulary = vocabularyLogic.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(command.userUid(), vocabulary);

        Term term = termLogic.create(command.toTermCreateCommand());

        return Result.from(term);
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            String term,
            String meaning,
            List<String> synonyms
    ) {
        public TermCreateCommand toTermCreateCommand() {
            return TermCreateCommand.builder()
                    .vocabularyUid(vocabularyUid)
                    .term(term)
                    .meaning(meaning)
                    .synonyms(synonyms)
                    .build();
        }
    }

    public record Result(
            String uid
    ) {
        public static Result from(Term term) {
            return new Result(term.getUid());
        }
    }
}
