package vook.server.api.web.routes.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.user.UserService;
import vook.server.api.app.domain.vocabulary.VocabularyService;
import vook.server.api.app.domain.vocabulary.data.VocabularyCreateCommand;
import vook.server.api.app.domain.vocabulary.data.VocabularyDeleteCommand;
import vook.server.api.app.domain.vocabulary.data.VocabularyUpdateCommand;
import vook.server.api.app.domain.user.model.User;
import vook.server.api.app.domain.vocabulary.model.Vocabulary;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyUpdateRequest;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VocabularyWebService {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public List<VocabularyResponse> vocabularies(VookLoginUser loginUser) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        List<Vocabulary> vocabularies = vocabularyService.findAllBy(user);
        return VocabularyResponse.from(vocabularies);
    }

    public void createVocabulary(VookLoginUser loginUser, VocabularyCreateRequest request) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        vocabularyService.create(VocabularyCreateCommand.of(request.getName(), user));
    }

    public void updateVocabulary(
            VookLoginUser loginUser,
            String vocabularyUid,
            VocabularyUpdateRequest request
    ) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        vocabularyService.update(VocabularyUpdateCommand.of(vocabularyUid, request.getName(), user));
    }

    public void deleteVocabulary(VookLoginUser loginUser, String vocabularyUid) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        vocabularyService.delete(VocabularyDeleteCommand.of(vocabularyUid, user));
    }
}
