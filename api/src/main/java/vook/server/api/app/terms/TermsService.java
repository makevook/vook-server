package vook.server.api.app.terms;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.terms.repo.TermsRepository;
import vook.server.api.model.terms.Terms;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository repository;

    public List<Terms> findAll() {
        return repository.findAll();
    }

    public Optional<Terms> find(Long id) {
        return repository.findById(id);
    }

    public boolean includeAllRequiredTerms(List<Long> agreeTermsId) {
        List<Terms> requiredTerms = repository.findAllByRequired(true);
        List<Long> requiredTermsId = requiredTerms.stream().map(Terms::getId).toList();
        return new HashSet<>(agreeTermsId).containsAll(requiredTermsId);
    }
}
