package vook.server.api.usecases.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.user.service.data.OnboardingCommand;
import vook.server.api.domain.vocabulary.model.TemplateTerm;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyName;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.TemplateVocabularyService;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.TermCreateAllCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OnboardingUserUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final TemplateVocabularyService templateVocabularyService;
    private final TermService termService;

    public void execute(Command command) {
        userService.onboarding(command.toOnboardingCommand());

        User user = userService.getByUid(command.userUid());
        TemplateVocabularyName vocabularyName = TemplateVocabularyName.from(command.job());
        Vocabulary vocabulary = vocabularyService.create(VocabularyCreateCommand.of(vocabularyName.name(), new UserId(user.getId())));

        List<TemplateTerm> terms = templateVocabularyService.getTermsByName(vocabularyName);
        termService.createAll(
                TermCreateAllCommand.builder()
                        .vocabularyUid(vocabulary.getUid())
                        .termInfos(
                                terms.stream()
                                        .map(term -> TermCreateAllCommand.TermInfo.builder()
                                                .term(term.getTerm())
                                                .meaning(term.getMeaning())
                                                .synonyms(term.getSynonyms())
                                                .build())
                                        .toList()
                        )
                        .build()
        );
    }

    public record Command(
            String userUid,
            Funnel funnel,
            Job job
    ) {
        public OnboardingCommand toOnboardingCommand() {
            return OnboardingCommand.of(userUid, funnel, job);
        }
    }
}
