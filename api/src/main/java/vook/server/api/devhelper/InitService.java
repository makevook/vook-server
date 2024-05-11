package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.*;
import vook.server.api.outbound.search.SearchClearable;
import vook.server.api.outbound.search.SearchService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final ResourceLoader resourceLoader;

    private final MemberRepository memberRepository;
    private final GlossaryRepository glossaryRepository;
    private final TermRepository termRepository;
    private final TermSynonymRepository termSynonymRepository;
    private final SearchClearable searchClearable;

    private final SearchService searchService;

    public void init() {
        termSynonymRepository.deleteAllInBatch();
        termRepository.deleteAllInBatch();
        glossaryRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        searchClearable.clearAll();

        Member vook = memberRepository.save(Member.forCreateOf("vook"));

        Glossary devGlossary = glossaryRepository.save(Glossary.forCreateOf("개발", vook));
        searchService.createGlossary(devGlossary);
        Glossary designGlossary = glossaryRepository.save(Glossary.forCreateOf("디자인", vook));
        searchService.createGlossary(designGlossary);
        Glossary marketingGlossary = glossaryRepository.save(Glossary.forCreateOf("마케팅", vook));
        searchService.createGlossary(marketingGlossary);
        Glossary practiceGlossary = glossaryRepository.save(Glossary.forCreateOf("실무", vook));
        searchService.createGlossary(practiceGlossary);

        List<Term> devTerms = getTerms("classpath:init/개발.tsv", devGlossary);
        List<Term> terms = termRepository.saveAll(devTerms);
        searchService.addTerms(terms, devGlossary);
    }

    private List<Term> getTerms(String location, Glossary glossary) {
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

    private static @NotNull List<Term> toTerms(List<RawTerm> rawTerms, Glossary glossary) {
        return rawTerms.stream()
                .map(rawTerm -> rawTerm.toTerm(glossary))
                .toList();
    }

    public static class RawTerm {
        private String term;
        private String synonyms;
        private String meaning;

        public Term toTerm(Glossary glossary) {
            Term term = Term.forCreateOf(this.term, this.meaning, glossary);
            Arrays.stream(this.synonyms.split("\\n"))
                    .map(String::trim)
                    .forEach(term::addSynonym);
            return term;
        }
    }
}
