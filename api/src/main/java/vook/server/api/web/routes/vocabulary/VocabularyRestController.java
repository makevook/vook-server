package vook.server.api.web.routes.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.common.web.CommonApiResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/vocabularies")
@RequiredArgsConstructor
public class VocabularyRestController implements VocabularyApi {

    private final VocabularyWebService service;

    @Override
    @GetMapping
    public CommonApiResponse<List<VocabularyResponse>> vocabularies(
            @AuthenticationPrincipal VookLoginUser user
    ) {
        List<VocabularyResponse> response = service.vocabularies(user);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PostMapping
    public CommonApiResponse<Void> createVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody VocabularyCreateRequest request
    ) {
        service.createVocabulary(user, request);
        return CommonApiResponse.ok();
    }

    @Override
    @PutMapping("/{vocabularyUid}")
    public CommonApiResponse<Void> updateVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String vocabularyUid,
            @Validated @RequestBody VocabularyUpdateRequest request
    ) {
        service.updateVocabulary(user, vocabularyUid, request);
        return CommonApiResponse.ok();
    }

    @Override
    @DeleteMapping("/{vocabularyUid}")
    public CommonApiResponse<Void> deleteVocabulary(
            @AuthenticationPrincipal VookLoginUser user,
            @PathVariable String vocabularyUid
    ) {
        service.deleteVocabulary(user, vocabularyUid);
        return CommonApiResponse.ok();
    }
}
