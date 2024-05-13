package vook.server.api.devhelper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vook.server.api.model.demo.DemoGlossaryRepository;

@Slf4j
@Profile("local")
@Component
@RequiredArgsConstructor
public class LocalInit {

    private final DemoGlossaryRepository demoGlossaryRepository;
    private final InitService initService;

    @PostConstruct
    public void init() {
        if (demoGlossaryRepository.count() > 0) {
            return;
        }

        initService.init();

        log.info("초기화 완료");
    }
}
