package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.demo.*;
import vook.server.api.outbound.search.DemoTermSearchService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final ResourceLoader resourceLoader;

    private final DemoGlossaryRepository demoGlossaryRepository;
    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
    private final DemoTermSearchService searchService;

    public void init() {
        demoTermSynonymRepository.deleteAllInBatch();
        demoTermRepository.deleteAllInBatch();
        demoGlossaryRepository.deleteAllInBatch();
        searchService.clearAll();

        DemoGlossary devGlossary = demoGlossaryRepository.save(DemoGlossary.forCreateOf("개발"));
        searchService.createGlossary(devGlossary);
        DemoGlossary designGlossary = demoGlossaryRepository.save(DemoGlossary.forCreateOf("디자인"));
        searchService.createGlossary(designGlossary);
        DemoGlossary marketingGlossary = demoGlossaryRepository.save(DemoGlossary.forCreateOf("마케팅"));
        searchService.createGlossary(marketingGlossary);
        DemoGlossary practiceGlossary = demoGlossaryRepository.save(DemoGlossary.forCreateOf("실무"));
        searchService.createGlossary(practiceGlossary);

        List<DemoTerm> devTerms = getTerms("classpath:init/개발.tsv", devGlossary);
        List<DemoTerm> terms = demoTermRepository.saveAll(devTerms);
        searchService.addTerms(devGlossary, terms);
    }

    private List<DemoTerm> getTerms(String location, DemoGlossary glossary) {
        try {
            // file로 바로 접근 할 경우, IDE에서는 접근 가능하나, jar로 패키징 후 실행 시에는 접근 불가능
            // ref) https://velog.io/@haron/트러블슈팅-Spring-IDE-에서-되는데-배포하면-안-돼요
            InputStream tsvFileInputStream = resourceLoader.getResource(location).getInputStream();
            CsvReader tsvReader = new CsvReader("\t");
            List<RawTerm> rawTerms = tsvReader.readValue(tsvFileInputStream, RawTerm.class);
            return toTerms(rawTerms, glossary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull List<DemoTerm> toTerms(List<RawTerm> rawTerms, DemoGlossary glossary) {
        return rawTerms.stream()
                .map(rawTerm -> rawTerm.toTerm(glossary))
                .toList();
    }

    public static class RawTerm {
        private String term;
        private String synonyms;
        private String meaning;

        public DemoTerm toTerm(DemoGlossary glossary) {
            DemoTerm term = DemoTerm.forCreateOf(this.term, this.meaning, glossary);
            String[] synonymArray = this.synonyms.split("//n");
            Arrays.stream(synonymArray)
                    .map(String::trim)
                    .forEach(term::addSynonym);
            return term;
        }
    }
}
