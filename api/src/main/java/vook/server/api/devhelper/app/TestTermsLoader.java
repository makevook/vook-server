package vook.server.api.devhelper.app;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vook.server.api.app.context.demo.model.DemoTerm;
import vook.server.api.devhelper.helper.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestTermsLoader {

    private final ResourceLoader resourceLoader;

    public List<DemoTerm> getTerms(String location) {
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
