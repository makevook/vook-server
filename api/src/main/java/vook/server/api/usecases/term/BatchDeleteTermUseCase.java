package vook.server.api.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchDeleteTermUseCase {

    private final UserService userService;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermService termService;

    public void execute(Command command) {
        validate(command.userUid(), command.termUids());
        termService.batchDelete(command.termUids());
    }

    private void validate(String userUid, List<String> termUids) {
        userService.validateCompletedUserByUid(userUid);
        termUids.forEach(termUid -> {
            Term term = termService.getByUid(termUid);
            vocabularyPolicy.validateOwner(userUid, term.getVocabulary());
        });
    }

    public record Command(
            String userUid,
            List<String> termUids
    ) {
    }
}
