package vook.server.api.domain.vocabulary.service;

import vook.server.api.domain.vocabulary.model.Term;

import java.util.List;

public interface TermSearchService {
    void saveTerm(Term term);

    void update(Term term);

    void delete(Term term);

    void saveAll(List<Term> terms);
}
