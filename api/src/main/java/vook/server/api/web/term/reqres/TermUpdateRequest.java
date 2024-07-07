package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vook.server.api.usecases.term.UpdateTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.List;

public record TermUpdateRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String term,

        @NotBlank
        @Size(min = 1, max = 2000)
        String meaning,

        @Valid
        @Size(max = 10)
        List<@Size(min = 1, max = 100) String> synonyms
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
