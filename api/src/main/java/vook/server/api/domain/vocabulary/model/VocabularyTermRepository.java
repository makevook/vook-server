package vook.server.api.domain.vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularyTermRepository extends JpaRepository<VocabularyTerm, Long> {
    Optional<VocabularyTerm> findByTermId(TermId termId);
}
