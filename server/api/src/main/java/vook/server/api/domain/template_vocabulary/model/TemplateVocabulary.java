package vook.server.api.domain.template_vocabulary.model;

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
    private TemplateVocabularyType type;

    public static TemplateVocabulary forCreateOf(
            TemplateVocabularyType type
    ) {
        TemplateVocabulary result = new TemplateVocabulary();
        result.type = type;
        return result;
    }
}
