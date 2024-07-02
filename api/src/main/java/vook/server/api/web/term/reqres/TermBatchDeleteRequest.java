package vook.server.api.web.term.reqres;

import vook.server.api.usecases.term.BatchDeleteTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.List;

public record TermBatchDeleteRequest(
        List<String> termUids
) {
    public BatchDeleteTermUseCase.Command toCommand(VookLoginUser user) {
        return new BatchDeleteTermUseCase.Command(user.getUid(), termUids);
    }
}
