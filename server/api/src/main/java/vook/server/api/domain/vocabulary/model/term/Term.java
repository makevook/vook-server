package vook.server.api.domain.vocabulary.model.term;

import jakarta.persistence.*;
import lombok.*;
import vook.server.api.domain.common.model.BaseEntity;
import vook.server.api.domain.common.model.Synonym;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;

import java.util.List;

@Getter
@Entity
@Table(name = "term")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Term extends BaseEntity {

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
    private Synonym synonym;

    @ManyToOne
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    public void update(Term term) {
        this.term = term.getTerm();
        this.meaning = term.getMeaning();
        this.synonym = term.getSynonym();
    }

    public List<String> getSynonyms() {
        return synonym.synonyms();
    }
}
