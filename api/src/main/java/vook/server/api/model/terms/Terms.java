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

    private Long version;

    public static Terms of(
            String title,
            String content,
            Long version
    ) {
        Terms result = new Terms();
        result.title = title;
        result.content = content;
        result.version = version;
        return result;
    }
}
