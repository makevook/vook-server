package vook.server.api.web.routes.user.reqres;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import vook.server.api.app.user.RegisterCommand;
import vook.server.api.model.terms.Terms;

import java.util.List;
import java.util.Optional;

@Data
public class UserRegisterRequest {

    @Schema
    private String nickname;

    @Schema
    private List<TermsAgree> termsAgrees;

    public RegisterCommand toCommand(String userUid, TermsFinder termsFinder) {
        return RegisterCommand.of(
                userUid,
                nickname,
                termsAgrees.stream().map(termsAgree -> {
                    Terms terms = termsFinder.find(termsAgree.id).orElseThrow();
                    return RegisterCommand.TermsAgree.of(terms, termsAgree.agree);
                }).toList()
        );
    }

    @FunctionalInterface
    public interface TermsFinder {
        Optional<Terms> find(Long id);
    }

    @Schema(hidden = true)
    public List<Long> getAgreeTermsId() {
        return termsAgrees.stream()
                .filter(t -> t.agree)
                .map(t -> t.id)
                .toList();
    }

    @Data
    public static class TermsAgree {
        private Long id;
        private Boolean agree;

        public static TermsAgree of(
                Long id,
                Boolean agree
        ) {
            TermsAgree result = new TermsAgree();
            result.id = id;
            result.agree = agree;
            return result;
        }
    }
}
