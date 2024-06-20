package vook.server.api.model.term;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "term_synonym")
public class TermSynonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 동의어
     */
    @Column(length = 100, nullable = false)
    private String synonym;

    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    static TermSynonym forCreateOf(
            String synonym,
            Term term
    ) {
        TermSynonym result = new TermSynonym();
        result.synonym = synonym;
        result.term = term;
        return result;
    }

}
