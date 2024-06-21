package vook.server.api.app.contexts.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 데모 용어 동의어
 */
@Getter
@Entity
@Table(name = "demo_term_synonym")
public class DemoTermSynonym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 동의어
     */
    @Column(length = 100, nullable = false)
    private String synonym;

    @ManyToOne
    @JoinColumn(name = "demo_term_id", nullable = false)
    private DemoTerm demoTerm;

    static DemoTermSynonym forCreateOf(
            String synonym,
            DemoTerm demoTerm
    ) {
        DemoTermSynonym result = new DemoTermSynonym();
        result.synonym = synonym;
        result.demoTerm = demoTerm;
        return result;
    }
}
