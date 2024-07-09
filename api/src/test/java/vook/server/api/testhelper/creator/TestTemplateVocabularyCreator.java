package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.logic.TemplateVocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.TemplateVocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyName;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class TestTemplateVocabularyCreator {

    private final TemplateVocabularyLogic vocabularyService;

    public void createTemplateVocabulary() {
        createTemplateVocabulary(TemplateVocabularyName.DEVELOPMENT);
        createTemplateVocabulary(TemplateVocabularyName.DESIGN);
        createTemplateVocabulary(TemplateVocabularyName.MARKETING);
        createTemplateVocabulary(TemplateVocabularyName.GENERAL_OFFICE);
    }

    private void createTemplateVocabulary(TemplateVocabularyName name) {
        String prefix = name.name();
        vocabularyService.create(
                new TemplateVocabularyCreateCommand(
                        name,
                        List.of(
                                new TemplateVocabularyCreateCommand.Term(prefix + "1", prefix + "뜻1", List.of(prefix + "동의어1", prefix + "동의어2")),
                                new TemplateVocabularyCreateCommand.Term(prefix + "2", prefix + "뜻2", List.of(prefix + "동의어3", prefix + "동의어4"))
                        )
                )
        );
    }
}
