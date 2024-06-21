package vook.server.api.app.domain.term.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.term.Term;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByUid(String uid);
}
