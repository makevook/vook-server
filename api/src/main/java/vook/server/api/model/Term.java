package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
@Table(name = "term")
public class Term extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String term;
    private String meaning;

    @ManyToOne
    @JoinColumn(name = "glossary_id", nullable = false)
    private Glossary glossary;

    @OneToMany(mappedBy = "term", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TermSynonym> synonym;

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

    public void addSynonym(TermSynonym termSynonym) {
        synonym.add(termSynonym);
    }
}
