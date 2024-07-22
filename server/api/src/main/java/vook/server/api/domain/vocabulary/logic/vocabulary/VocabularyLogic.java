package vook.server.api.domain.vocabulary.logic.vocabulary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyFactory;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyRepository;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.List;

@DomainLogic
@RequiredArgsConstructor
public class VocabularyLogic {

    private final VocabularyFactory vocabularyFactory;
    private final VocabularyRepository repository;
    private final TermRepository termRepository;
    private final SearchManagementService searchService;

    public List<Vocabulary> findAllBy(@NotNull UserUid userUid) {
        return repository.findAllByUserUid(userUid);
    }

    public List<String> findAllUidsBy(@NotNull UserUid userUid) {
        return repository.findAllUidsByUserUid(userUid);
    }

    public Vocabulary create(@NotNull @Valid VocabularyCreateCommand command) {
        Vocabulary saved = repository.save(vocabularyFactory.create(command.name(), command.userUid()));
        searchService.save(saved);
        return saved;
    }

    public void update(@NotNull @Valid VocabularyUpdateCommand command) {
        Vocabulary vocabulary = getVocabulary(command.vocabularyUid());
        vocabulary.update(command.name());
    }

    public void delete(@NotBlank String vocabularyUid) {
        Vocabulary vocabulary = getVocabulary(vocabularyUid);
        List<String> termUids = termRepository.findByVocabulary(vocabulary).stream().map(Term::getUid).toList();
        termRepository.deleteAllByUids(termUids);
        repository.delete(vocabulary);
        searchService.delete(vocabulary);
    }

    public Vocabulary getByUid(@NotBlank String vocabularyUid) {
        return getVocabulary(vocabularyUid);
    }

    private Vocabulary getVocabulary(String vocabularyUid) {
        return repository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
    }

    public interface SearchManagementService {
        void save(Vocabulary saved);

        void delete(Vocabulary vocabulary);
    }
}
