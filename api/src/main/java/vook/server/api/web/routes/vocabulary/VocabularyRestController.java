package vook.server.api.web.routes.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;

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
}
