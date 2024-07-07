@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "user",
                "vocabulary"
        }
)
package vook.server.api.usecases;

import org.springframework.modulith.ApplicationModule;
