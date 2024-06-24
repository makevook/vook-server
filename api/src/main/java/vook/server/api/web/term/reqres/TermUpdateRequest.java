package vook.server.api.web.term.reqres;

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
    private String termUid;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    private List<String> synonyms = new ArrayList<>();

    public UpdateTermUseCase.Command toCommand(VookLoginUser loginUser) {
        return new UpdateTermUseCase.Command(
                loginUser.getUid(),
                termUid,
                term,
                meaning,
                synonyms
        );
    }
}
