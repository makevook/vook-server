package vook.server.api.domain.vocabulary.logic.term;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.term.TermFactory;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.domain.vocabulary.service.SearchManagementService;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.List;

@DomainLogic
@RequiredArgsConstructor
public class TermLogic {

    private final TermFactory termFactory;
    private final TermRepository termRepository;
    private final SearchManagementService searchManagementService;

    public Term create(@NotNull TermCreateCommand command) {
        Term term = command.toEntity(termFactory);
        Term saved = termRepository.save(term);
        searchManagementService.save(saved);
        return saved;
    }

    public void createAll(@NotNull TermCreateAllCommand command) {
        List<Term> terms = command.toEntity(termFactory);
        termRepository.saveAll(terms);
        searchManagementService.saveAll(terms);
    }

    public Term getByUid(@NotBlank String uid) {
        return getTermByUid(uid);
    }

    public void update(@NotNull TermUpdateCommand serviceCommand) {
        Term term = getTermByUid(serviceCommand.uid());
        Term updateTerm = serviceCommand.toEntity(termFactory);
        term.update(updateTerm);
        searchManagementService.update(term);
    }

    public void delete(@NotBlank String uid) {
        Term term = getTermByUid(uid);
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

    private Term getTermByUid(String uid) {
        return termRepository.findByUid(uid).orElseThrow(TermNotFoundException::new);
    }
}
