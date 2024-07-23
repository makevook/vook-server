package vook.server.api.devhelper.app.sync;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.demo.model.DemoTerm;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.infra.search.demo.MeilisearchDemoTermSearchService;
import vook.server.api.infra.search.vocabulary.MeilisearchSearchManagementService;

import java.util.List;

import static vook.server.api.domain.demo.model.QDemoTerm.demoTerm;
import static vook.server.api.domain.demo.model.QDemoTermSynonym.demoTermSynonym;
import static vook.server.api.domain.vocabulary.model.term.QTerm.term1;
import static vook.server.api.domain.vocabulary.model.vocabulary.QVocabulary.vocabulary;

@Service
@Transactional
@RequiredArgsConstructor
public class SyncService {

    private final MeilisearchDemoTermSearchService demoSearchService;
    private final MeilisearchSearchManagementService searchManagementService;
    private final VocabularyRepo vocabularyRepo;

    public void sync() {
        searchManagementService.clearAll();

        demoSearchService.init();
        demoSearchService.addTerms(vocabularyRepo.findAllDemoTerm());

        vocabularyRepo.findAllVocabulary().forEach(saved -> {
            searchManagementService.save(saved);
            searchManagementService.saveAll(saved.getTerms());
        });
    }

    @Repository
    @RequiredArgsConstructor
    public static class VocabularyRepo {

        private final JPAQueryFactory queryFactory;

        public List<DemoTerm> findAllDemoTerm() {
            return queryFactory.selectFrom(demoTerm)
                    .join(demoTerm.synonyms, demoTermSynonym).fetchJoin()
                    .fetch();
        }

        public List<Vocabulary> findAllVocabulary() {
            return queryFactory.selectFrom(vocabulary)
                    .join(vocabulary.terms, term1).fetchJoin()
                    .fetch();
        }
    }
}
