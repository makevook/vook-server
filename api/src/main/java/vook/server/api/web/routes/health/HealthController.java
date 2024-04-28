package vook.server.api.web.routes.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController implements HealthApi {

    @GetMapping
    public String health() {
        return "OK";
    }
}
