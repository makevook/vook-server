@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Web Common",
        allowedDependencies = {
                "vook.server.api.domain.user",
        }
)
package vook.server.api.web.common;

import org.springframework.modulith.ApplicationModule;
