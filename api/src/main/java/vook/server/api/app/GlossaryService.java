package vook.server.api.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.Glossary;
import vook.server.api.model.GlossaryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final GlossaryRepository repository;

    public List<Glossary> findAll() {
        return repository.findAll();
    }
}
