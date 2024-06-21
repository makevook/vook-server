package vook.server.api.app.contexts.term.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByUid(String uid);
}
