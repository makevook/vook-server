package vook.server.api.web.term.reqres;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.term.usecase.SearchTermUseCase;

import java.util.List;

public record TermSearchRequest(
        @Valid
        @NotEmpty
        List<@NotBlank String> vocabularyUids,

        @Valid
        @NotEmpty
        List<@NotBlank String> queries,

        Boolean withFormat,

        String highlightPreTag,

        String highlightPostTag
) {
    public SearchTermUseCase.Command toCommand(VookLoginUser loginUser) {
        return SearchTermUseCase.Command.builder()
                .userUid(loginUser.getUid())
                .vocabularyUids(vocabularyUids)
                .queries(queries)
                .withFormat(withFormat != null && withFormat)
                .highlightPreTag(highlightPreTag)
                .highlightPostTag(highlightPostTag)
                .build();
    }
}
