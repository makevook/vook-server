package vook.server.api.app.contexts.term.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.app.contexts.term.application.data.TermCreateCommand;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.term.domain.TermRepository;
import vook.server.api.app.contexts.term.domain.VocabularyId;
import vook.server.api.app.contexts.term.exception.TermLimitExceededException;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public Term create(@Valid TermCreateCommand command) {
        int termCount = termRepository.countByVocabularyId(command.getVocabularyId());
        if (termCount >= 100) {
            throw new TermLimitExceededException();
        }

        Term saved = termRepository.save(command.toEntity());
        saved.addAllSynonym(command.getSynonyms());
        return saved;
    }

    public int countByVocabularyId(VocabularyId vocabularyId) {
        return termRepository.countByVocabularyId(vocabularyId);
    }
}
