package vook.server.api.domain.vocabulary.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.annotations.Formula;
import vook.server.api.domain.common.model.BaseEntity;

import java.util.HashSet;
import java.util.Set;
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
    @AttributeOverride(name = "value", column = @Column(name = "user_uid", nullable = false))
    private UserUid userUid;

    @OneToMany(mappedBy = "vocabulary", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Term> terms = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @Formula("(SELECT COUNT(t.id) FROM term t WHERE t.vocabulary_id = id)")
    private int termCount;

    public static Vocabulary forCreateOf(
            String name,
            UserUid userUid
    ) {
        Vocabulary result = new Vocabulary();
        result.uid = UUID.randomUUID().toString();
        result.name = name;
        result.userUid = userUid;
        return result;
    }

    public boolean isValidOwner(UserUid userUid) {
        return this.userUid.equals(userUid);
    }

    public void update(String name) {
        this.name = name;
    }

    public void addTerm(Term term) {
        this.terms.add(term);
        termCount++;
    }

    public int termCount() {
        return this.termCount;
    }

    public void removeTerm(Term term) {
        this.terms.remove(term);
        termCount--;
    }
}
