package vook.server.api.domain.vocabulary.model.vocabulary;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import vook.server.api.domain.common.model.BaseEntity;
import vook.server.api.domain.vocabulary.model.term.Term;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "vocabulary")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uid;

    /**
     * 용어집 이름
     */
    @Column(length = 20, nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "user_uid", nullable = false, unique = true)
    )
    private UserUid userUid;

    @Builder.Default
    @OneToMany(mappedBy = "vocabulary", fetch = FetchType.LAZY)
    private List<Term> terms = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Formula("(SELECT COUNT(t.id) FROM term t WHERE t.vocabulary_id = id)")
    private int termCount;

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
