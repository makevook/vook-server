package vook.server.api.domain.vocabulary.logic;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.logic.dto.TermCreateAllCommand;
import vook.server.api.domain.vocabulary.logic.dto.TermCreateCommand;
import vook.server.api.domain.vocabulary.logic.dto.TermUpdateCommand;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.List;

@DomainLogic
@RequiredArgsConstructor
public class TermLogic {

    private final TermRepository termRepository;
    private final VocabularyRepository vocabularyRepository;
    private final SearchManagementService searchManagementService;

    public Term create(@Valid TermCreateCommand command) {
        Term term = command.toEntity(vocabularyRepository::findByUid);
        int termCount = term.getVocabulary().termCount();
        if (termCount >= 100) {
            throw new TermLimitExceededException();
        }
        Term saved = termRepository.save(term);
        searchManagementService.save(saved);
        return saved;
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
        searchManagementService.saveAll(terms);
    }

    public Term getByUid(@NotBlank String uid) {
        return termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
    }

    public void update(@Valid TermUpdateCommand serviceCommand) {
        Term term = termRepository.findByUid(serviceCommand.uid()).orElseThrow(TermNotFoundException::new);
        Term updateTerm = serviceCommand.toEntity();
        term.update(updateTerm);
        searchManagementService.update(term);
    }

    public void delete(@NotBlank String uid) {
        Term term = termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
        term.getVocabulary().removeTerm(term);
        termRepository.delete(term);
        searchManagementService.delete(term);
    }

    public void batchDelete(
            @Valid
            @NotEmpty
            List<@NotBlank String> termUids
    ) {
        List<Term> terms = termRepository.findByUidIn(termUids);
        terms.forEach(term -> term.getVocabulary().removeTerm(term));
        termRepository.deleteAll(terms);
        searchManagementService.deleteAll(terms);
    }

    public interface SearchManagementService {
        void save(Term term);

        void update(Term term);

        void delete(Term term);

        void saveAll(List<Term> terms);

        void deleteAll(List<Term> terms);
    }
}
