package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "template_term")
public class TemplateTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 용어 이름
     */
    @Column(length = 100, nullable = false)
    private String term;

    /**
     * 용어 의미
     */
    @Column(length = 2000, nullable = false)
    private String meaning;

    /**
     * 동의어
     */
    @Getter(AccessLevel.PRIVATE)
    @Column(length = 1100)
    private Synonym synonym;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_vocabulary_id", nullable = false)
    private TemplateVocabulary templateVocabulary;

    public static TemplateTerm forCreateOf(
            String term,
            String meaning,
            List<String> synonyms,
            TemplateVocabulary templateVocabulary
    ) {
        TemplateTerm result = new TemplateTerm();
        result.term = term;
        result.meaning = meaning;
        result.synonym = Synonym.from(synonyms);
        result.templateVocabulary = templateVocabulary;
        return result;
    }

    public List<String> getSynonyms() {
        return this.synonym.synonyms();
    }
}
