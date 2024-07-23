package vook.server.api.devhelper.web.routes.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.devhelper.app.init.InitService;
import vook.server.api.devhelper.app.sync.SyncService;
import vook.server.api.web.common.response.CommonApiResponse;

@Profile({"local", "dev", "stag"})
@RestController
@RequestMapping("/dev-helper")
@RequiredArgsConstructor
public class DevHelperController implements DevHelperApi {

    private final InitService initService;
    private final SyncService syncService;

    @Override
    @PostMapping("/init")
    public CommonApiResponse<Void> init() {
        initService.init();
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/sync")
    public CommonApiResponse<Void> sync() {
        syncService.sync();
        return CommonApiResponse.ok();
    }
}
