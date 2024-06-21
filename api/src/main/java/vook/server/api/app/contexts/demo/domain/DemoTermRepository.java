package vook.server.api.app.contexts.demo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoTermRepository extends JpaRepository<DemoTerm, Long> {
}
