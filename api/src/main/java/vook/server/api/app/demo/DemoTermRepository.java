package vook.server.api.app.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.demo.DemoTerm;

public interface DemoTermRepository extends JpaRepository<DemoTerm, Long> {
}
