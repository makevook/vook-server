@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "demo"
        }
)
package vook.server.api.infra;

import org.springframework.modulith.ApplicationModule;
