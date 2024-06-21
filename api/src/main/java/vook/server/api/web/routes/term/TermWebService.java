package vook.server.api.web.routes.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.term.TermService;
import vook.server.api.app.domain.user.UserService;
import vook.server.api.app.domain.vocabulary.VocabularyService;
import vook.server.api.app.domain.term.model.Term;
import vook.server.api.app.domain.user.model.User;
import vook.server.api.app.domain.vocabulary.model.Vocabulary;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.term.reqres.TermCreateRequest;
import vook.server.api.web.routes.term.reqres.TermCreateResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class TermWebService {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final TermService termService;

    public TermCreateResponse create(VookLoginUser loginUser, TermCreateRequest request) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        Vocabulary vocabulary = vocabularyService.findByUidAndUser(request.getVocabularyUid(), user);
        Term term = termService.create(request.toCommand(vocabulary));
        return TermCreateResponse.from(term);
    }
}
