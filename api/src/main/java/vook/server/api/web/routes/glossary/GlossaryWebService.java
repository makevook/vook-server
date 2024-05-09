package vook.server.api.web.routes.glossary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.GlossaryService;
import vook.server.api.model.Glossary;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GlossaryWebService {

    private final GlossaryService glossaryService;

    public List<Glossary> retrieve() {
        return glossaryService.findAll();
    }
}
