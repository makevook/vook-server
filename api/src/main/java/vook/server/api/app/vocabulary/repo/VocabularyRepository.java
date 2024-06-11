package vook.server.api.app.vocabulary.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;

import java.util.List;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findAllByUser(User user);
}
