package vook.server.api.domain.vocabulary.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.domain.vocabulary.service.data.TermCreateAllCommand;
import vook.server.api.domain.vocabulary.service.data.TermCreateCommand;
import vook.server.api.domain.vocabulary.service.data.TermUpdateCommand;
import vook.server.api.globalcommon.annotation.DomainService;

import java.util.List;

@DomainService
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

    public void createAll(@Valid TermCreateAllCommand command) {
        Vocabulary vocabulary = vocabularyRepository.findByUid(command.vocabularyUid()).orElseThrow();
        int savedCount = vocabulary.termCount();
        int count = command.termInfos().size();
        if (savedCount + count > 100) {
            throw new TermLimitExceededException();
        }

        List<Term> terms = command.termInfos().stream()
                .map(term -> term.toEntity(vocabulary))
                .toList();
        termRepository.saveAll(terms);
    }

    public Term getByUid(@NotBlank String uid) {
        return termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
    }

    public void update(@Valid TermUpdateCommand serviceCommand) {
        Term term = termRepository.findByUid(serviceCommand.uid()).orElseThrow(TermNotFoundException::new);
        Term updateTerm = serviceCommand.toEntity();
        term.update(updateTerm);
    }

    public void delete(@NotBlank String uid) {
        Term term = termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
        term.getVocabulary().removeTerm(term);
        termRepository.delete(term);
    }
}
