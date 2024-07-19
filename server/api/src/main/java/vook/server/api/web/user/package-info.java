@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "User Web",
        allowedDependencies = {
                "vook.server.api.web.common",
                "vook.server.api.domain.user",
                "vook.server.api.domain.vocabulary",
                "vook.server.api.domain.template_vocabulary"
        }
)
package vook.server.api.web.user;

import org.springframework.modulith.ApplicationModule;
