package vook.server.api.web.routes.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.user.UserService;
import vook.server.api.app.vocabulary.VocabularyService;
import vook.server.api.app.vocabulary.data.VocabularyCreateCommand;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VocabularyWebService {

    private final UserService userService;
    private final VocabularyService service;

    public List<VocabularyResponse> vocabularies(VookLoginUser loginUser) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        List<Vocabulary> vocabularies = service.findAllBy(user);
        return VocabularyResponse.from(vocabularies);
    }

    public void createVocabulary(VookLoginUser loginUser, VocabularyCreateRequest request) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        service.create(VocabularyCreateCommand.of(request.getName(), user));
    }
}
