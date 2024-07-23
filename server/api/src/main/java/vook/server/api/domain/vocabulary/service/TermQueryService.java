package vook.server.api.domain.vocabulary.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vook.server.api.domain.vocabulary.model.term.Term;

public interface TermQueryService {
    Page<Term> findAllBy(String vocabularyUid, Pageable params);
}
