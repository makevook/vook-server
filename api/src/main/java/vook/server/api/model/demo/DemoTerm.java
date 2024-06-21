package vook.server.api.model.demo;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.common.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 데모 용어
 */
@Getter
@Entity
@Table(name = "demo_term")
public class DemoTerm extends BaseEntity {

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

    @OneToMany(mappedBy = "demoTerm", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemoTermSynonym> synonyms = new ArrayList<>();

    public static DemoTerm forCreateOf(
            String term,
            String meaning
    ) {
        DemoTerm result = new DemoTerm();
        result.term = term;
        result.meaning = meaning;
        return result;
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(DemoTermSynonym.forCreateOf(synonym, this));
    }
}
