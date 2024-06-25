package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vook.server.api.usecases.term.CreateTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.ArrayList;
import java.util.List;

@Data
public class TermCreateRequest {

    @NotBlank
    private String vocabularyUid;

    @NotBlank
    @Size(min = 1, max = 100)
    private String term;

    @NotBlank
    @Size(min = 1, max = 2000)
    private String meaning;

    @Valid
    @Size(max = 10)
    private List<@Size(min = 1, max = 100) String> synonyms = new ArrayList<>();

    public CreateTermUseCase.Command toCommand(VookLoginUser loginUser) {
        return new CreateTermUseCase.Command(
                loginUser.getUid(),
                vocabularyUid,
                term,
                meaning,
                synonyms
        );
    }
}
