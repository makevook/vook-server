@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        allowedDependencies = {
                "demo",
                "user",
                "usecases"
        }
)
package vook.server.api.web;

import org.springframework.modulith.ApplicationModule;
