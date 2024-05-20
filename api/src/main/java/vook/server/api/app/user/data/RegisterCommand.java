package vook.server.api.app.user.data;

import lombok.Getter;
import vook.server.api.model.terms.Terms;

import java.util.List;

@Getter
public class RegisterCommand {

    private String userUid;
    private String nickname;
    private List<TermsAgree> termsAgrees;

    public static RegisterCommand of(
            String userUid,
            String nickname,
            List<TermsAgree> termsAgrees
    ) {
        RegisterCommand command = new RegisterCommand();
        command.userUid = userUid;
        command.nickname = nickname;
        command.termsAgrees = termsAgrees;
        return command;
    }

    @Getter
    public static class TermsAgree {
        private Terms terms;
        private Boolean agree;

        public static TermsAgree of(
                Terms terms,
                Boolean agree
        ) {
            TermsAgree result = new TermsAgree();
            result.terms = terms;
            result.agree = agree;
            return result;
        }
    }
}
