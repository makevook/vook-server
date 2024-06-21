package vook.server.api.app.context.term.domain;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.app.context.vocabulary.domain.Vocabulary;
import vook.server.api.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "term")
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

    @ManyToOne
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @OneToMany(mappedBy = "term", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermSynonym> synonyms = new ArrayList<>();

    public static Term forCreateOf(
            String term,
            String meaning,
            Vocabulary vocabulary
    ) {
        Term result = new Term();
        result.uid = UUID.randomUUID().toString();
        result.term = term;
        result.meaning = meaning;
        result.vocabulary = vocabulary;
        vocabulary.addTerm(result);
        return result;
    }

    public void addAllSynonym(List<String> synonyms) {
        synonyms.forEach(s -> this.synonyms.add(TermSynonym.forCreateOf(s, this)));
    }
}
