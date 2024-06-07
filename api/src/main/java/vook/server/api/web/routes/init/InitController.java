package vook.server.api.web.routes.init;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.devhelper.InitService;
import vook.server.api.web.common.CommonApiResponse;

@Profile({"local", "dev", "stag"})
@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController implements InitApi {

    private final InitService initService;

    @PostMapping
    public CommonApiResponse<Void> init() {
        initService.init();
        return CommonApiResponse.ok();
    }
}
