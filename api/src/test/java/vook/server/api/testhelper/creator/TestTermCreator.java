package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.term.model.Term;
import vook.server.api.domain.term.model.VocabularyId;
import vook.server.api.domain.term.service.TermService;
import vook.server.api.domain.term.service.data.TermCreateCommand;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class TestTermCreator {

    private final TermService termService;

    public Term createTerm(Vocabulary vocabulary) {
        return termService.create(TermCreateCommand.builder()
                .vocabularyId(new VocabularyId(vocabulary.getId()))
                .term("testTerm")
                .meaning("testMeaning")
                .synonyms(List.of("testSynonym"))
                .build());
    }
}
