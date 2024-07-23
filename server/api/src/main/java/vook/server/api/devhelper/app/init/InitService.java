package vook.server.api.devhelper.app.init;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.devhelper.app.sync.SyncService;
import vook.server.api.domain.user.model.social_user.SocialUserRepository;
import vook.server.api.domain.user.model.user.UserRepository;
import vook.server.api.domain.user.model.user_info.UserInfoRepository;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.infra.vocabulary.cache.UserVocabularyCacheRepository;
import vook.server.api.infra.vocabulary.jpa.VocabularyJpaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final TermRepository termRepository;
    private final VocabularyJpaRepository vocabularyJpaRepository;
    private final UserVocabularyCacheRepository userVocabularyCacheRepository;
    private final UserInfoRepository userInfoRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;

    private final SyncService syncService;

    public void init() {
        deleteAllUserData();
        syncService.sync();
    }

    private void deleteAllUserData() {
        // 용어집
        termRepository.deleteAllInBatch();
        vocabularyJpaRepository.deleteAllInBatch();
        userVocabularyCacheRepository.deleteAll();

        // 사용자
        userInfoRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}
