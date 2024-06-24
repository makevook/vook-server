@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "user",
                "vocabulary",
                "term"
        }
)
package vook.server.api.usecases;

import org.springframework.modulith.ApplicationModule;
