@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "demo",
                "vocabulary",
                "usecases"
        }
)
package vook.server.api.infra;

import org.springframework.modulith.ApplicationModule;
