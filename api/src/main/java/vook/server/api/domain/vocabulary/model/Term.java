package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import vook.server.api.domain.common.model.BaseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "term")
public class Term extends BaseEntity {

    public static final String SYNONYM_DELIMITER = ":,:";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

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
    private String synonym;

    @ManyToOne
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    public static Term forCreateOf(
            String term,
            String meaning,
            List<String> synonyms,
            Vocabulary vocabulary
    ) {
        Term result = new Term();
        result.uid = UUID.randomUUID().toString();
        result.term = term;
        result.meaning = meaning;
        result.addAllSynonym(synonyms);
        result.vocabulary = vocabulary;
        vocabulary.addTerm(result);
        return result;
    }

    public static Term forUpdateOf(
            String term,
            String meaning,
            List<String> synonyms
    ) {
        Term result = new Term();
        result.term = term;
        result.meaning = meaning;
        result.addAllSynonym(synonyms);
        return result;
    }

    public void update(Term term) {
        this.term = term.getTerm();
        this.meaning = term.getMeaning();
        this.synonym = term.getSynonym();
    }

    private void addAllSynonym(List<String> input) {
        this.synonym = String.join(SYNONYM_DELIMITER, input);
    }

    public List<String> getSynonyms() {
        if (synonym == null || synonym.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(synonym.split(SYNONYM_DELIMITER)).toList();
    }
}
