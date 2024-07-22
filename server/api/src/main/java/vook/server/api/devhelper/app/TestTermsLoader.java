package vook.server.api.devhelper.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vook.server.api.devhelper.helper.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestTermsLoader {

    private final ResourceLoader resourceLoader;

    public <T> List<T> getTerms(String location, Converter<T> converter) {
        try {
            // file로 바로 접근 할 경우, IDE에서는 접근 가능하나, jar로 패키징 후 실행 시에는 접근 불가능
            // ref) https://velog.io/@haron/트러블슈팅-Spring-IDE-에서-되는데-배포하면-안-돼요
            InputStream tsvFileInputStream = resourceLoader.getResource(location).getInputStream();
            CsvReader tsvReader = new CsvReader("\t");
            List<RawTerm> rawTerms = tsvReader.readValue(tsvFileInputStream, RawTerm.class);
            return converter.convert(rawTerms);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    public static class RawTerm {
        private String term;
        private String synonyms;
        private String meaning;
    }

    public interface Converter<T> {
        List<T> convert(List<RawTerm> rawTerm);
    }
}
