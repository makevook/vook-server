package vook.server.api.web.routes.glossary;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.web.common.CommonApiResponse;

import java.util.List;

@RestController
@RequestMapping("/glossary")
@RequiredArgsConstructor
public class GlossaryRestController implements GlossaryApi {

    private final GlossaryWebService service;

    @Override
    @GetMapping
    public CommonApiResponse<List<RetrieveResponse>> retrieve() {
        List<RetrieveResponse> result = service.retrieve();
        return CommonApiResponse.okWithResult(result);
    }

    @Override
    @GetMapping("/{glossaryUid}/terms")
    public CommonApiResponse<List<RetrieveTermsResponse>> retrieveTerms(
            @PathVariable String glossaryUid,
            @PageableDefault(size = Integer.MAX_VALUE, sort = "term") Pageable pageable
    ) {
        List<RetrieveTermsResponse> result = service.retrieveTerms(glossaryUid, pageable);
        return CommonApiResponse.okWithResult(result);
    }
}
