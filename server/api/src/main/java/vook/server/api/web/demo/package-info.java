@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Demo Web",
        allowedDependencies = {
                "vook.server.api.web.common",
                "vook.server.api.domain.demo",
        }
)
package vook.server.api.web.demo;

import org.springframework.modulith.ApplicationModule;
