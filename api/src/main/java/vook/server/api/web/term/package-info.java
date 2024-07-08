@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Term Web",
        allowedDependencies = {
                "vook.server.api.web.common",
                "vook.server.api.domain.user",
                "vook.server.api.domain.vocabulary",
        }
)
package vook.server.api.web.term;

import org.springframework.modulith.ApplicationModule;
