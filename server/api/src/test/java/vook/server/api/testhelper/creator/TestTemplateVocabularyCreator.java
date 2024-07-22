package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.template_vocabulary.logic.TemplateVocabularyLogic;
import vook.server.api.domain.template_vocabulary.logic.dto.TemplateVocabularyCreateCommand;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyType;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class TestTemplateVocabularyCreator {

    private final TemplateVocabularyLogic vocabularyService;

    public void createTemplateVocabulary() {
        createTemplateVocabulary(TemplateVocabularyType.DEVELOPMENT);
        createTemplateVocabulary(TemplateVocabularyType.DESIGN);
        createTemplateVocabulary(TemplateVocabularyType.MARKETING);
        createTemplateVocabulary(TemplateVocabularyType.GENERAL_OFFICE);
    }

    private void createTemplateVocabulary(TemplateVocabularyType type) {
        String prefix = type.name();
        vocabularyService.create(
                new TemplateVocabularyCreateCommand(
                        type,
                        List.of(
                                new TemplateVocabularyCreateCommand.Term(prefix + "1", prefix + "뜻1", List.of(prefix + "동의어1", prefix + "동의어2")),
                                new TemplateVocabularyCreateCommand.Term(prefix + "2", prefix + "뜻2", List.of(prefix + "동의어3", prefix + "동의어4"))
                        )
                )
        );
    }
}
