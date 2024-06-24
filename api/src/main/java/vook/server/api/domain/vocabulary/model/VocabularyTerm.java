package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "vocabulary_term")
public class VocabularyTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "term_id", nullable = false))
    private TermId termId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    public static VocabularyTerm forCreateOf(
            TermId termId,
            Vocabulary vocabulary
    ) {
        VocabularyTerm result = new VocabularyTerm();
        result.termId = termId;
        result.vocabulary = vocabulary;
        return result;
    }
}
