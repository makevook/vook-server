package vook.server.api.web.term.reqres;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import vook.server.api.usecases.term.RetrieveTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

@Data
public class TermRetrieveRequest {

    private String vocabularyUid;

    public RetrieveTermUseCase.Command toCommand(VookLoginUser user, Pageable pageable) {
        return new RetrieveTermUseCase.Command(user.getUid(), vocabularyUid, pageable);
    }
}
