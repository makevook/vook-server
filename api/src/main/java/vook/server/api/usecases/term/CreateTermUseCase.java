package vook.server.api.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.TermCreateCommand;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTermUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final TermService termService;
    private final VocabularyPolicy vocabularyPolicy;

    public Result execute(Command command) {
        userService.validateCompletedUserByUid(command.userUid());
        
        Vocabulary vocabulary = vocabularyService.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(command.userUid(), vocabulary);

        Term term = termService.create(command.toTermCreateCommand());

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
