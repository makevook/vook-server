package vook.server.api.domain.vocabulary.logic.dto;

import vook.server.api.domain.vocabulary.model.TemplateTerm;
import vook.server.api.domain.vocabulary.model.TemplateVocabulary;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyName;

import java.util.List;

public record TemplateVocabularyCreateCommand(
        TemplateVocabularyName name,
        List<Term> terms
) {

    public TemplateVocabulary toVocabulary() {
        return TemplateVocabulary.forCreateOf(name);
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
