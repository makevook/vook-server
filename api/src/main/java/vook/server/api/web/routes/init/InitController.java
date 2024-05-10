package vook.server.api.web.routes.init;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.devhelper.InitService;

@Profile({"local", "dev"})
@RestController
@RequestMapping("/init")
@RequiredArgsConstructor
public class InitController implements InitApi {

    private final InitService initService;

    @PostMapping
    public void init() {
        initService.init();
    }
}
