package vook.server.api.model.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DemoGlossaryRepository extends JpaRepository<DemoGlossary, Long> {
    Optional<DemoGlossary> findByUid(String uid);
}
