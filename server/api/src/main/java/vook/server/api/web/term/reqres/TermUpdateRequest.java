package vook.server.api.web.term.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.term.usecase.UpdateTermUseCase;

import java.util.List;

public record TermUpdateRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String term,

        @NotBlank
        @Size(min = 1, max = 2000)
        String meaning,

        List<String> synonyms
) {
    public UpdateTermUseCase.Command toCommand(VookLoginUser loginUser, String termUid) {
        return new UpdateTermUseCase.Command(
                loginUser.getUid(),
                termUid,
                term,
                meaning,
                synonyms == null ? List.of() : synonyms
        );
    }
}
