package vook.server.api.app.terms;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.terms.Terms;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}
