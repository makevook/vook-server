package vook.server.api.web.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.usecases.term.CreateTermUseCase;
import vook.server.api.usecases.term.UpdateTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.term.reqres.TermCreateRequest;
import vook.server.api.web.term.reqres.TermCreateResponse;
import vook.server.api.web.term.reqres.TermUpdateRequest;

@Slf4j
@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermRestController implements TermApi {

    private final CreateTermUseCase createTermUseCase;
    private final UpdateTermUseCase updateTermUseCase;

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

    @Override
    @PutMapping
    public CommonApiResponse<Void> update(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody TermUpdateRequest request
    ) {
        var command = request.toCommand(user);
        updateTermUseCase.execute(command);
        return CommonApiResponse.ok();
    }
}
