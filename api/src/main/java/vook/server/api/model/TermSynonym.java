package vook.server.api.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "term_synonym")
public class TermSynonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String synonym;

    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    public static TermSynonym forCreateOf(
            String synonym,
            Term term
    ) {
        TermSynonym result = new TermSynonym();
        result.synonym = synonym;
        result.term = term;
        term.addSynonym(result);
        return result;
    }
}
