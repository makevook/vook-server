package vook.server.api.app.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.demo.DemoTermSynonym;

public interface DemoTermSynonymRepository extends JpaRepository<DemoTermSynonym, Long> {
}
