package vook.server.api.web.routes.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.app.crosscontext.usecases.term.CreateTermUseCase;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.term.reqres.TermCreateRequest;
import vook.server.api.web.routes.term.reqres.TermCreateResponse;

@Slf4j
@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermRestController implements TermApi {

    private final CreateTermUseCase createTermUseCase;

    @Override
    @PostMapping
    public CommonApiResponse<TermCreateResponse> create(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody TermCreateRequest request
    ) {
        var command = request.toCommand(user);
        var result = createTermUseCase.execute(command);
        var response = TermCreateResponse.from(result);
        return CommonApiResponse.okWithResult(response);
    }
}
