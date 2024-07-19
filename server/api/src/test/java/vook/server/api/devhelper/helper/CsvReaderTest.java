package vook.server.api.devhelper.helper;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CsvReaderTest {

    @Test
    @DisplayName("TSV Reader - 문자열 변환")
    void readValueString() {
        // given
        String csvString = """
                term\tsynonyms\tdescription
                Java\t자바\t자바는 객체지향 프로그래밍 언어이다.
                Spring\t스프링\t스프링은 자바 기반의 프레임워크이다.
                """;

        CsvReader reader = new CsvReader("\t");

        // when
        List<Term> entity = reader.readValue(csvString, Term.class);

        // then
        assertThat(entity).hasSize(2);
        assertThat(entity).extracting("term", "synonyms", "description")
                .containsExactly(
                        tuple("Java", "자바", "자바는 객체지향 프로그래밍 언어이다."),
                        tuple("Spring", "스프링", "스프링은 자바 기반의 프레임워크이다.")
                );
    }

    @Test
    @DisplayName("TSV Reader - 파일 변환")
    void readValueFile() {
        // given
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tsvreader/test.tsv").getFile());

        CsvReader reader = new CsvReader("\t");

        // when
        List<Term> entity = reader.readValue(file, Term.class);

        // then
        assertThat(entity).hasSize(2);
        assertThat(entity).extracting("term", "synonyms", "description")
                .containsExactly(
                        tuple("Java", "자바", "자바는 객체지향 프로그래밍 언어이다."),
                        tuple("Spring", "스프링", "스프링은 자바 기반의 프레임워크이다.")
                );
    }

    @Test
    @DisplayName("TSV Reader - InputStream 변환")
    void readValueInputStream() throws IOException {
        // given
        InputStream inputStream = new ClassPathResource("tsvreader/test.tsv").getInputStream();

        CsvReader reader = new CsvReader("\t");

        // when
        List<Term> entity = reader.readValue(inputStream, Term.class);

        // then
        assertThat(entity).hasSize(2);
        assertThat(entity).extracting("term", "synonyms", "description")
                .containsExactly(
                        tuple("Java", "자바", "자바는 객체지향 프로그래밍 언어이다."),
                        tuple("Spring", "스프링", "스프링은 자바 기반의 프레임워크이다.")
                );
    }

    @Test
    @DisplayName("TSV Reader - 개행처리")
    void readValueWithNewLine() {
        // given
        String csvString = """
                term\tsynonyms\tdescription
                "Ja\\nva"\t"자\\n바"\t"자바는 객체지향 \\n프로그래밍 언어이다."
                Spr\\ning\t"스프\\n링"\t스프링은 자바 기반의 프레임워크이다.
                """;

        CsvReader reader = new CsvReader("\t");

        // when
        List<Term> entity = reader.readValue(csvString, Term.class);

        // then
        assertThat(entity).hasSize(2);
        assertThat(entity).extracting("term", "synonyms", "description")
                .containsExactlyInAnyOrder(
                        tuple("Ja\nva", "자\n바", "자바는 객체지향 \n프로그래밍 언어이다."),
                        tuple("Spr\\ning", "스프\n링", "스프링은 자바 기반의 프레임워크이다.")
                );
    }

    @Getter
    static class Term {
        private String term;
        private String synonyms;
        private String description;
    }
}
