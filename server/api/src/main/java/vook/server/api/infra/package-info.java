@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Infra",
        allowedDependencies = {
                "vook.server.api.domain.demo",
                "vook.server.api.domain.vocabulary",
                "vook.server.api.web.term",
        }
)
package vook.server.api.infra;

import org.springframework.modulith.ApplicationModule;
