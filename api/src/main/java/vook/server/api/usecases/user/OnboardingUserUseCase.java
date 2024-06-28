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

        User user = userService.getCompletedUserByUid(command.userUid());
        TemplateVocabularyName vocabularyName = vocabularyNameFrom(command.job());
        Vocabulary vocabulary = vocabularyService.create(
                VocabularyCreateCommand.builder()
                        .name(vocabularyName.name())
                        .userId(new UserId(user.getId()))
                        .build()
        );

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

    private TemplateVocabularyName vocabularyNameFrom(Job job) {
        return switch (job) {
            case PLANNER, DESIGNER -> TemplateVocabularyName.DEVELOPMENT;
            case MARKETER -> TemplateVocabularyName.MARKETING;
            case DEVELOPER -> TemplateVocabularyName.DESIGN;
            case CEO, HR, OTHER -> TemplateVocabularyName.GENERAL_OFFICE;
            case null -> TemplateVocabularyName.GENERAL_OFFICE;
        };
    }

    public record Command(
            String userUid,
            Funnel funnel,
            Job job
    ) {
        public OnboardingCommand toOnboardingCommand() {
            return OnboardingCommand.builder()
                    .userUid(userUid)
                    .funnel(funnel)
                    .job(job)
                    .build();
        }
    }
}
