package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 용어
 */
@Getter
@Entity
@Table(name = "term")
public class Term extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "glossary_id", nullable = false)
    private Glossary glossary;

    @OneToMany(mappedBy = "term", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermSynonym> synonyms = new ArrayList<>();

    public static Term forCreateOf(
            String term,
            String meaning,
            Glossary glossary
    ) {
        Term result = new Term();
        result.term = term;
        result.meaning = meaning;
        result.glossary = glossary;
        return result;
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(TermSynonym.forCreateOf(synonym, this));
    }
}
