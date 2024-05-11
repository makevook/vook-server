package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.*;
import vook.server.api.outbound.search.SearchClearable;
import vook.server.api.outbound.search.SearchService;

import java.io.File;
import java.io.IOException;
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
        termRepository.saveAll(devTerms);
    }

    private List<Term> getTerms(String location, Glossary glossary) {
        try {
            File tsvFile = resourceLoader.getResource(location).getFile();
            CsvReader tsvReader = new CsvReader("\t");
            List<RawTerm> rawTerms = tsvReader.readValue(tsvFile, RawTerm.class);
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
