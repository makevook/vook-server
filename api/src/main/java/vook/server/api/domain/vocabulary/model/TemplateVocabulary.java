package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "template_vocabulary")
public class TemplateVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 용어집 이름
     */
    @Column(nullable = false, unique = true, columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private TemplateVocabularyName name;

    public static TemplateVocabulary forCreateOf(
            TemplateVocabularyName name
    ) {
        TemplateVocabulary result = new TemplateVocabulary();
        result.name = name;
        return result;
    }
}
