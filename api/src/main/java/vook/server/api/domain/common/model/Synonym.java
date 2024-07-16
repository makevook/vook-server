package vook.server.api.domain.common.model;

import jakarta.persistence.Embeddable;

import java.util.Arrays;
import java.util.List;

@Embeddable
public class Synonym {

    private static final String SYNONYM_DELIMITER = ":,:";

    private String synonym;

    public static Synonym from(List<String> input) {
        Synonym result = new Synonym();
        result.synonym = String.join(SYNONYM_DELIMITER, input);
        return result;
    }

    public List<String> synonyms() {
        if (synonym == null || synonym.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(synonym.split(SYNONYM_DELIMITER)).toList();
    }
}
