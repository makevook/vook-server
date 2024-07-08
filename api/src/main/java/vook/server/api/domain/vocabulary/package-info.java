@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Vocabulary Domain",
        allowedDependencies = {
                "vook.server.api.domain.common"
        }
)
package vook.server.api.domain.vocabulary;

import org.springframework.modulith.ApplicationModule;
