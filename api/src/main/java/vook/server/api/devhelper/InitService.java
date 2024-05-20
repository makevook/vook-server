package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.demo.DemoTermRepository;
import vook.server.api.app.demo.DemoTermSynonymRepository;
import vook.server.api.app.terms.TermsRepository;
import vook.server.api.app.user.SocialUserRepository;
import vook.server.api.app.user.UserRepository;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.model.terms.Terms;
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

    private final DemoTermRepository demoTermRepository;
    private final DemoTermSynonymRepository demoTermSynonymRepository;
    private final SocialUserRepository socialUserRepository;
    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final DemoTermSearchService searchService;

    public void init() {
        demoTermSynonymRepository.deleteAllInBatch();
        demoTermRepository.deleteAllInBatch();
        socialUserRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        termsRepository.deleteAllInBatch();
        searchService.clearAll();

        termsRepository.save(Terms.of("이용약관", loadContents("classpath:init/이용약관.txt"), true));
        termsRepository.save(Terms.of("개인정보 수집 이용 약관", loadContents("classpath:init/개인정보_수집_이용_약관.txt"), true));
        termsRepository.save(Terms.of("마케팅 메일 수신 약관", loadContents("classpath:init/마케팅_메일_수신_약관.txt"), false));

        List<DemoTerm> devTerms = getTerms("classpath:init/개발.tsv");
        demoTermRepository.saveAll(devTerms);

        searchService.init();
        searchService.addTerms(devTerms);
    }

    private String loadContents(String location) {
        try {
            InputStream inputStream = resourceLoader.getResource(location).getInputStream();
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DemoTerm> getTerms(String location) {
        try {
            // file로 바로 접근 할 경우, IDE에서는 접근 가능하나, jar로 패키징 후 실행 시에는 접근 불가능
            // ref) https://velog.io/@haron/트러블슈팅-Spring-IDE-에서-되는데-배포하면-안-돼요
            InputStream tsvFileInputStream = resourceLoader.getResource(location).getInputStream();
            CsvReader tsvReader = new CsvReader("\t");
            List<RawTerm> rawTerms = tsvReader.readValue(tsvFileInputStream, RawTerm.class);
            return toTerms(rawTerms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<DemoTerm> toTerms(List<RawTerm> rawTerms) {
        return rawTerms.stream()
                .map(RawTerm::toTerm)
                .toList();
    }

    public static class RawTerm {
        private String term;
        private String synonyms;
        private String meaning;

        public DemoTerm toTerm() {
            DemoTerm term = DemoTerm.forCreateOf(this.term, this.meaning);
            String[] synonymArray = this.synonyms.split("//n");
            Arrays.stream(synonymArray)
                    .map(String::trim)
                    .forEach(term::addSynonym);
            return term;
        }
    }
}
