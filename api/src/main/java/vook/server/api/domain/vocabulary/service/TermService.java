package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.domain.vocabulary.service.data.TermCreateCommand;
import vook.server.api.domain.vocabulary.service.data.TermUpdateCommand;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;
    private final VocabularyRepository vocabularyRepository;

    public Term create(@Valid TermCreateCommand command) {
        Term term = command.toEntity(vocabularyRepository::findByUid);
        int termCount = term.getVocabulary().termCount();
        if (termCount >= 100) {
            throw new TermLimitExceededException();
        }
        return termRepository.save(term);
    }

    public Term getByUid(String uid) {
        return termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
    }

    public void update(TermUpdateCommand serviceCommand) {
        Term term = termRepository.findByUid(serviceCommand.getUid()).orElseThrow(TermNotFoundException::new);
        Term updateTerm = serviceCommand.toEntity();
        term.update(updateTerm);
    }

    public void delete(String uid) {
        Term term = termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
        term.getVocabulary().removeTerm(term);
        termRepository.delete(term);
    }
}
