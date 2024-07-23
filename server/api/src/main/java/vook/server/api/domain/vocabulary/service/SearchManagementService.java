package vook.server.api.domain.vocabulary.service;

import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;

import java.util.List;

public interface SearchManagementService {
    
    void save(Vocabulary saved);

    void delete(Vocabulary vocabulary);

    void save(Term term);

    void update(Term term);

    void delete(Term term);

    void saveAll(List<Term> terms);

    void deleteAll(List<Term> terms);
}
