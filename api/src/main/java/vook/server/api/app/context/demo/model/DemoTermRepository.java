package vook.server.api.app.context.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoTermRepository extends JpaRepository<DemoTerm, Long> {
}
