package vook.server.api.web.routes.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveGlossariesResponse;
import vook.server.api.web.routes.demo.reqres.RetrieveTermsResponse;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

import java.util.List;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoRestController implements DemoApi {

    private final DemoWebService service;

    @Override
    @GetMapping("/glossaries")
    public CommonApiResponse<List<RetrieveGlossariesResponse>> retrieveGlossaries() {
        List<RetrieveGlossariesResponse> result = service.retrieveGlossaries();
        return CommonApiResponse.okWithResult(result);
    }

    @Override
    @GetMapping("/glossaries/{glossaryUid}/terms")
    public CommonApiResponse<List<RetrieveTermsResponse>> retrieveTerms(
            @PathVariable String glossaryUid,
            @PageableDefault(size = Integer.MAX_VALUE, sort = "term") Pageable pageable
    ) {
        List<RetrieveTermsResponse> result = service.retrieveTerms(glossaryUid, pageable);
        return CommonApiResponse.okWithResult(result);
    }

    @Override
    @PostMapping("/glossaries/{glossaryUid}/terms/search")
    public CommonApiResponse<SearchTermResponse> searchTerm(
            @PathVariable String glossaryUid,
            @RequestBody SearchTermRequest request
    ) {
        SearchTermResponse result = service.searchTerm(glossaryUid, request);
        return CommonApiResponse.okWithResult(result);
    }
}
