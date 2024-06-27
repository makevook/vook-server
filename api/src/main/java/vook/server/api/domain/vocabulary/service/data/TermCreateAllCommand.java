package vook.server.api.domain.vocabulary.service.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;

@Getter
public class TermCreateAllCommand {

    @NotNull
    private String vocabularyUid;

    @Valid
    @NotEmpty
    private List<TermInfo> termInfos;

    @Builder
    public static TermCreateAllCommand of(String vocabularyUid, List<TermInfo> termInfos) {
        TermCreateAllCommand command = new TermCreateAllCommand();
        command.vocabularyUid = vocabularyUid;
        command.termInfos = termInfos;
        return command;
    }

    public static class TermInfo {
        @NotBlank
        @Size(min = 1, max = 100)
        private String term;

        @NotBlank
        @Size(min = 1, max = 2000)
        private String meaning;

        @NotNull
        @Valid
        @Size(max = 10)
        private List<@Size(min = 1, max = 100) String> synonyms;

        @Builder
        public static TermInfo of(String term, String meaning, List<String> synonyms) {
            TermInfo termInfo = new TermInfo();
            termInfo.term = term;
            termInfo.meaning = meaning;
            termInfo.synonyms = synonyms;
            return termInfo;
        }

        public Term toEntity(Vocabulary vocabulary) {
            return Term.forCreateOf(term, meaning, synonyms, vocabulary);
        }
    }
}
