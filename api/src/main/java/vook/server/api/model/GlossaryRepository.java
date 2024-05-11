package vook.server.api.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlossaryRepository extends JpaRepository<Glossary, Long> {
    Optional<Glossary> findByUid(String uid);
}
