package vook.server.api.outbound.search.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "service.meilisearch")
public class MeilisearchProperties {
    private String host;
    private String apiKey;
}
