package vook.server.api.app;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermSearchRepository searchRepository;

    public List<Term> findAllBy(Glossary glossary, Pageable pageable) {
        return searchRepository.search(glossary.getUid(), pageable);
    }
}
