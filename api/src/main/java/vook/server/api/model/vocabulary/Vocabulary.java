package vook.server.api.model.vocabulary;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.model.common.BaseEntity;
import vook.server.api.model.user.User;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "vocabulary", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Term> terms = new ArrayList<>();

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

    public void addTerm(Term term) {
        this.terms.add(term);
    }

    public int termCount() {
        return this.terms.size();
    }

    public boolean isValidOwner(User user) {
        return this.user.equals(user);
    }

    public void update(String name) {
        this.name = name;
    }
}
