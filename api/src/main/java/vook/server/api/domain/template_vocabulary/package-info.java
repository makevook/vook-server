@ApplicationModule(
        type = ApplicationModule.Type.OPEN,
        displayName = "Template Vocabulary Domain",
        allowedDependencies = {
                "vook.server.api.domain.common"
        }
)
package vook.server.api.domain.template_vocabulary;

import org.springframework.modulith.ApplicationModule;
