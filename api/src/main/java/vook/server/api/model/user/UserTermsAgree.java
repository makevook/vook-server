package vook.server.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.model.terms.Terms;

@Getter
@Entity
@Table(name = "user_terms_agree")
public class UserTermsAgree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    private Terms terms;

    private Boolean agree;

    public static UserTermsAgree of(
            User user,
            Terms terms,
            Boolean agree
    ) {
        UserTermsAgree result = new UserTermsAgree();
        result.user = user;
        result.terms = terms;
        result.agree = agree;
        return result;
    }
}
