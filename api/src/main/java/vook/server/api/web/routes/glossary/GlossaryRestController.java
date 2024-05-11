package vook.server.api.web.routes.glossary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.model.Glossary;
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
        List<Glossary> result = service.retrieve();
        return CommonApiResponse.okWithResult(RetrieveResponse.from(result));
    }

    @Override
    @GetMapping("/{glossaryUid}/terms")
    public CommonApiResponse<List<FindAllTermsResponse>> findAllTerms(@PathVariable String glossaryUid) {
        List<FindAllTermsResponse> result = service.findAllTerms(glossaryUid);
        return CommonApiResponse.okWithResult(result);
    }
}
