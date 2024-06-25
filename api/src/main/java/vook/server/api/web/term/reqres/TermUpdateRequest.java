package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vook.server.api.usecases.term.UpdateTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.ArrayList;
import java.util.List;

@Data
public class TermUpdateRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    @Valid
    @Size(max = 10)
    private List<@Size(min = 1, max = 100) String> synonyms = new ArrayList<>();

    public UpdateTermUseCase.Command toCommand(VookLoginUser loginUser, String termUid) {
        return new UpdateTermUseCase.Command(
                loginUser.getUid(),
                termUid,
                term,
                meaning,
                synonyms
        );
    }
}
