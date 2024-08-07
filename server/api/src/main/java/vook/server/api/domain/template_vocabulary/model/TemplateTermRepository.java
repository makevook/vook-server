package vook.server.api.domain.template_vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateTermRepository extends JpaRepository<TemplateTerm, Long> {
    List<TemplateTerm> findByTemplateVocabulary(TemplateVocabulary templateVocabulary);
}
