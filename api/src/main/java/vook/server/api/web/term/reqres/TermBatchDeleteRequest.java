package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.term.usecase.BatchDeleteTermUseCase;

import java.util.List;

public record TermBatchDeleteRequest(
        @Valid
        @NotEmpty
        List<@NotEmpty String> termUids
) {
    public BatchDeleteTermUseCase.Command toCommand(VookLoginUser user) {
        return new BatchDeleteTermUseCase.Command(user.getUid(), termUids);
    }
}
