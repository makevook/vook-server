package vook.server.api.app.terms;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.terms.Terms;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {
    List<Terms> findAllByRequired(Boolean required);
}
