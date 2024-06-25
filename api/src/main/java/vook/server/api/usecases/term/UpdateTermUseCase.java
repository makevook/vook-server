package vook.server.api.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.domain.vocabulary.service.data.TermUpdateCommand;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTermUseCase {

    private final UserService userService;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermService termService;

    public void execute(Command command) {
        User user = userService.getByUid(command.userUid());
        Term term = termService.getByUid(command.termUid());
        vocabularyPolicy.validateOwner(user, term.getVocabulary());

        termService.update(command.toServiceCommand());
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
