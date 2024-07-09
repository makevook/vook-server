package vook.server.api.domain.vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateVocabularyRepository extends JpaRepository<TemplateVocabulary, Long> {
    Optional<TemplateVocabulary> findByType(TemplateVocabularyType type);
}
