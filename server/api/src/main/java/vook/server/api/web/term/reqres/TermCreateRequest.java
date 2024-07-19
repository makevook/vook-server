package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.term.usecase.CreateTermUseCase;

import java.util.List;

public record TermCreateRequest(
        @NotBlank
        String vocabularyUid,

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
    public CreateTermUseCase.Command toCommand(VookLoginUser loginUser) {
        return new CreateTermUseCase.Command(
                loginUser.getUid(),
                vocabularyUid,
                term,
                meaning,
                synonyms == null ? List.of() : synonyms
        );
    }
}
