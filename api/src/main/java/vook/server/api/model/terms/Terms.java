package vook.server.api.model.terms;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "terms")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean required;

    public static Terms of(
            String title,
            String content,
            Boolean required
    ) {
        Terms result = new Terms();
        result.title = title;
        result.content = content;
        result.required = required;
        return result;
    }
}
