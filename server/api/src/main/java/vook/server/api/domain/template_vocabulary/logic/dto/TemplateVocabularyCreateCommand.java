package vook.server.api.domain.template_vocabulary.logic.dto;

import vook.server.api.domain.template_vocabulary.model.TemplateTerm;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabulary;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyType;

import java.util.List;

public record TemplateVocabularyCreateCommand(
        TemplateVocabularyType type,
        List<Term> terms
) {

    public TemplateVocabulary toVocabulary() {
        return TemplateVocabulary.forCreateOf(type);
    }

    public List<TemplateTerm> toTerms(TemplateVocabulary vocabulary) {
        return terms.stream()
                .map(term -> TemplateTerm.forCreateOf(
                        term.term(),
                        term.meaning(),
                        term.synonyms(),
                        vocabulary
                ))
                .toList();
    }

    public record Term(
            String term,
            String meaning,
            List<String> synonyms
    ) {
    }
}
