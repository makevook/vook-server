@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Auth Web",
        allowedDependencies = {
                "vook.server.api.web.common"
        }
)
package vook.server.api.web.auth;

import org.springframework.modulith.ApplicationModule;
