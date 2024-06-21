package vook.server.api.web.routes.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.demo.reqres.SearchTermRequest;
import vook.server.api.web.routes.demo.reqres.SearchTermResponse;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoRestController implements DemoApi {

    private final DemoWebService service;

    @Override
    @PostMapping("/terms/search")
    public CommonApiResponse<SearchTermResponse> searchTerm(
            @RequestBody SearchTermRequest request
    ) {
        SearchTermResponse result = service.searchTerm(request);
        return CommonApiResponse.okWithResult(result);
    }
}
