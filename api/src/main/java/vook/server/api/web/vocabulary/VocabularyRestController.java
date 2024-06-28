package vook.server.api.web.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.usecases.vocabulary.CreateVocabularyUseCase;
import vook.server.api.usecases.vocabulary.DeleteVocabularyUseCase;
import vook.server.api.usecases.vocabulary.RetrieveVocabularyUseCase;
import vook.server.api.usecases.vocabulary.UpdateVocabularyUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.vocabulary.reqres.VocabularyUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/vocabularies")
@RequiredArgsConstructor
public class VocabularyRestController implements VocabularyApi {

    private final RetrieveVocabularyUseCase retrieveVocabulary;
    private final CreateVocabularyUseCase createVocabulary;
    private final UpdateVocabularyUseCase updateVocabulary;
    private final DeleteVocabularyUseCase deleteVocabulary;

    @Override
    @GetMapping
    public CommonApiResponse<List<VocabularyResponse>> vocabularies(
            @AuthenticationPrincipal VookLoginUser user
    ) {
        var command = new RetrieveVocabularyUseCase.Command(user.getUid());
        var result = retrieveVocabulary.execute(command);
        List<VocabularyResponse> response = VocabularyResponse.from(result);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PostMapping
    public CommonApiResponse<Void> createVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody VocabularyCreateRequest request
    ) {
        var command = new CreateVocabularyUseCase.Command(user.getUid(), request.name());
        createVocabulary.execute(command);
        return CommonApiResponse.ok();
    }

    @Override
    @PutMapping("/{vocabularyUid}")
    public CommonApiResponse<Void> updateVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String vocabularyUid,
            @Validated @RequestBody VocabularyUpdateRequest request
    ) {
        var command = new UpdateVocabularyUseCase.Command(user.getUid(), vocabularyUid, request.name());
        updateVocabulary.execute(command);
        return CommonApiResponse.ok();
    }

    @Override
    @DeleteMapping("/{vocabularyUid}")
    public CommonApiResponse<Void> deleteVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String vocabularyUid
    ) {
        var command = new DeleteVocabularyUseCase.Command(user.getUid(), vocabularyUid);
        deleteVocabulary.execute(command);
        return CommonApiResponse.ok();
    }
}
