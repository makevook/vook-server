package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.domain.common.model.BaseEntity;

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

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "user_id", nullable = false))
    private UserId userId;

    public static Vocabulary forCreateOf(
            String name,
            UserId userId
    ) {
        Vocabulary result = new Vocabulary();
        result.uid = UUID.randomUUID().toString();
        result.name = name;
        result.userId = userId;
        return result;
    }

    public boolean isValidOwner(UserId userId) {
        return this.userId.equals(userId);
    }

    public void update(String name) {
        this.name = name;
    }
}
