package vook.server.api.app.contexts.vocabulary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Formula;
import vook.server.api.app.common.entity.BaseEntity;
import vook.server.api.app.contexts.user.domain.User;

import java.util.UUID;

@Getter
@Entity
@Table(name = "vocabulary")
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    /**
     * 용어집 이름
     */
    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Formula("(SELECT COUNT(t.id) FROM term t WHERE t.vocabulary_id = id)")
    private int termCount;

    public static Vocabulary forCreateOf(
            String name,
            User user
    ) {
        Vocabulary result = new Vocabulary();
        result.uid = UUID.randomUUID().toString();
        result.name = name;
        result.user = user;
        return result;
    }

    public int termCount() {
        return this.termCount;
    }

    public boolean isValidOwner(User user) {
        return this.user.equals(user);
    }

    public void update(String name) {
        this.name = name;
    }
}
