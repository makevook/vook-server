package vook.server.api.web.routes.term;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.web.common.CommonApiResponse;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermRestController implements TermRestApi {

    private final TermWebService service;

    @Override
    @PostMapping("/search")
    public CommonApiResponse<SearchResponse> search(
            @RequestBody SearchRequest request
    ) {
        SearchResponse result = service.search(request);
        return CommonApiResponse.okWithResult(result);
    }
}
