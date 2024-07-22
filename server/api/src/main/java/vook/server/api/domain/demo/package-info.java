@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Demo Domain",
        allowedDependencies = {
                "vook.server.api.domain.common"
        }
)
package vook.server.api.domain.demo;

import org.springframework.modulith.ApplicationModule;
