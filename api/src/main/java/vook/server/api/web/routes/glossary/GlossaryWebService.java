package vook.server.api.web.routes.glossary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.GlossaryService;
import vook.server.api.app.TermService;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GlossaryWebService {

    private final GlossaryService glossaryService;
    private final TermService termService;

    public List<Glossary> retrieve() {
        return glossaryService.findAll();
    }

    public List<FindAllTermsResponse> findAllTerms(String glossaryUid) {
        Glossary glossary = glossaryService.findByUid(glossaryUid).orElseThrow();
        List<Term> terms = termService.findAllBy(glossary);
        return FindAllTermsResponse.from(glossary, terms);
    }
}
