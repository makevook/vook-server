@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Vocabulary Web",
        allowedDependencies = {
                "vook.server.api.web.common",
                "vook.server.api.domain.user",
                "vook.server.api.domain.vocabulary"
        }
)
package vook.server.api.web.vocabulary;

import org.springframework.modulith.ApplicationModule;
