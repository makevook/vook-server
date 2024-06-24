package vook.server.api.domain.term.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.term.exception.TermLimitExceededException;
import vook.server.api.domain.term.model.Term;
import vook.server.api.domain.term.model.TermRepository;
import vook.server.api.domain.term.model.VocabularyId;
import vook.server.api.domain.term.service.data.TermCreateCommand;

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
