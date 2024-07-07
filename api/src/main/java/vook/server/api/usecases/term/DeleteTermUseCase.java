package vook.server.api.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteTermUseCase {

    private final UserService userService;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermService termService;

    public void execute(Command command) {
        userService.validateCompletedUserByUid(command.userUid());

        Term term = termService.getByUid(command.termUid());
        vocabularyPolicy.validateOwner(command.userUid(), term.getVocabulary());

        termService.delete(command.termUid());
    }

    public record Command(
            String userUid,
            String termUid
    ) {
    }
}
