package vook.server.api.app.terms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.terms.Terms;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository repository;

    public List<Terms> findAll() {
        return repository.findAll();
    }
}
