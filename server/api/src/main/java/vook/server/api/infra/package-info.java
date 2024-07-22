@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Infra",
        allowedDependencies = {
                "vook.server.api.domain.demo",
                "vook.server.api.domain.vocabulary"
        }
)
package vook.server.api.infra;

import org.springframework.modulith.ApplicationModule;
