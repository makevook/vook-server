@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "User Domain",
        allowedDependencies = {
                "vook.server.api.domain.common"
        }
)
package vook.server.api.domain.user;

import org.springframework.modulith.ApplicationModule;
