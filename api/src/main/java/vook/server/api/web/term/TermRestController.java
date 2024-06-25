package vook.server.api.web.term;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.usecases.term.CreateTermUseCase;
import vook.server.api.usecases.term.DeleteTermUseCase;
import vook.server.api.usecases.term.RetrieveTermUseCase;
import vook.server.api.usecases.term.UpdateTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.term.reqres.TermCreateRequest;
import vook.server.api.web.term.reqres.TermCreateResponse;
import vook.server.api.web.term.reqres.TermResponse;
import vook.server.api.web.term.reqres.TermUpdateRequest;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermRestController implements TermApi {

    private final CreateTermUseCase createTermUseCase;
    private final RetrieveTermUseCase retrieveTermUseCase;
    private final UpdateTermUseCase updateTermUseCase;
    private final DeleteTermUseCase deleteTermUseCase;

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
    @GetMapping
    public CommonApiResponse<List<TermResponse>> retrieve(
            @AuthenticationPrincipal VookLoginUser user,
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable,
            @RequestParam String vocabularyUid
    ) {
        var command = new RetrieveTermUseCase.Command(user.getUid(), vocabularyUid, pageable);
        var result = retrieveTermUseCase.execute(command);
        var response = TermResponse.from(result);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PutMapping("/{termUid}")
    public CommonApiResponse<Void> update(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String termUid,
            @Validated @RequestBody TermUpdateRequest request
    ) {
        var command = request.toCommand(user, termUid);
        updateTermUseCase.execute(command);
        return CommonApiResponse.ok();
    }

    @Override
    @DeleteMapping("/{termUid}")
    public CommonApiResponse<Void> delete(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String termUid
    ) {
        var command = new DeleteTermUseCase.Command(user.getUid(), termUid);
        deleteTermUseCase.execute(command);
        return CommonApiResponse.ok();
    }
}
