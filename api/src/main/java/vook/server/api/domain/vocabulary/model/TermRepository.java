package vook.server.api.domain.vocabulary.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    Optional<Term> findByUid(String uid);

    List<Term> findByUidIn(List<String> termUids);

    List<Term> findByVocabulary(Vocabulary vocabulary);

    @Modifying
    @Query("delete from Term t where t.uid in :uids")
    void deleteAllByUids(@Param("uids") List<String> termUids);
}
