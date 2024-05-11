package vook.server.api.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;
import vook.server.api.model.TermRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository repository;

    public List<Term> findAllBy(Glossary glossary) {
        return repository.findAllByGlossary(glossary);
    }
}
