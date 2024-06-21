package vook.server.api.app.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.term.application.TermService;
import vook.server.api.app.contexts.term.application.data.TermCreateCommand;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTermUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final TermService termService;

    public Result create(Command command) {
        User user = userService.findByUid(command.userUid()).orElseThrow();
        Vocabulary vocabulary = vocabularyService.findByUidAndUser(command.vocabularyUid(), user);
        Term term = termService.create(command.toServiceCommand(vocabulary));
        return Result.from(term);
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            String term,
            String meaning,
            List<String> synonyms
    ) {
        public TermCreateCommand toServiceCommand(Vocabulary vocabulary) {
            return TermCreateCommand.of(vocabulary, term, meaning, synonyms);
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
