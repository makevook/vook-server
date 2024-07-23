package vook.server.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.keyvalue.repository.KeyValueRepository;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = "vook.server.api",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {KeyValueRepository.class})
)
public class JpaConfig {
}
