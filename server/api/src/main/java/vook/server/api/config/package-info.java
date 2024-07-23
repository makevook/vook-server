@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "vook.server.api.web.common",
                "vook.server.api.infra"
        }
)
package vook.server.api.config;

import org.springframework.modulith.ApplicationModule;
