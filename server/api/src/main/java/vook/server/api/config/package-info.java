@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "vook.server.api.web.common"
        }
)
package vook.server.api.config;

import org.springframework.modulith.ApplicationModule;
