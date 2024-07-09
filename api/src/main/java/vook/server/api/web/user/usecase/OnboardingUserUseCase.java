package vook.server.api.web.user.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.user.logic.dto.UserOnboardingCommand;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.vocabulary.logic.TemplateVocabularyLogic;
import vook.server.api.domain.vocabulary.logic.TermLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.TermCreateAllCommand;
import vook.server.api.domain.vocabulary.logic.dto.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.TemplateTerm;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyName;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OnboardingUserUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;
    private final TemplateVocabularyLogic templateVocabularyLogic;
    private final TermLogic termLogic;

    public void execute(Command command) {
        userLogic.onboarding(command.toOnboardingCommand());

        userLogic.validateCompletedUserByUid(command.userUid());

        TemplateVocabularyName vocabularyName = vocabularyNameFrom(command.job());
        Vocabulary vocabulary = vocabularyLogic.create(
                VocabularyCreateCommand.builder()
                        .name(vocabularyName.name())
                        .userUid(new UserUid(command.userUid()))
                        .build()
        );

        List<TemplateTerm> terms = templateVocabularyLogic.getTermsByName(vocabularyName);
        termLogic.createAll(
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
        public UserOnboardingCommand toOnboardingCommand() {
            return UserOnboardingCommand.builder()
                    .userUid(userUid)
                    .funnel(funnel)
                    .job(job)
                    .build();
        }
    }
}
