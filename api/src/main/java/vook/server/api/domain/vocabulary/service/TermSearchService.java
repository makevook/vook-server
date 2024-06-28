package vook.server.api.domain.vocabulary.service;

import vook.server.api.domain.vocabulary.model.Term;

public interface TermSearchService {
    void saveTerm(Term term);

    void update(Term term);

    void delete(Term term);
}
