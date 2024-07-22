package vook.server.api.domain.vocabulary.model.term;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface TermFactory {

    Term create(@NotNull @Valid CreateCommand request);

    record CreateCommand(
            @NotNull
            String vocabularyUid,

            @Valid
            @NotNull
            TermInfo termInfo
    ) {
    }

    List<Term> createForBatchCreate(@NotNull @Valid CreateForBatchCommand request);

    record CreateForBatchCommand(
            @NotNull
            String vocabularyUid,

            @Valid
            List<@Valid @NotNull TermInfo> termInfos
    ) {
    }

    Term createForUpdate(@NotNull @Valid UpdateCommand request);

    record UpdateCommand(
            @Valid
            @NotNull
            TermInfo termInfo
    ) {
    }

    record TermInfo(
            @NotBlank
            @Size(min = 1, max = 100)
            String term,

            @NotBlank
            @Size(min = 1, max = 2000)
            String meaning,

            @NotNull
            List<String> synonyms
    ) {
        @AssertTrue(message = "동의어는 콤마(,)포함 2000자 이내로 입력해주세요.")
        boolean isSynonymsSizeValid() {
            if (synonyms == null) {
                return true;
            }
            return String.join(",", synonyms).length() <= 2000;
        }
    }
}
