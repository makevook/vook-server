package vook.server.api.web.term.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import vook.server.api.usecases.term.SearchTermUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.List;

public record TermSearchRequest(
        @NotEmpty
        List<String> vocabularyUids,

        @NotBlank
        String query,

        Boolean withFormat,

        String highlightPreTag,

        String highlightPostTag
) {
    public SearchTermUseCase.Command toCommand(VookLoginUser loginUser) {
        return SearchTermUseCase.Command.builder()
                .userUid(loginUser.getUid())
                .vocabularyUids(vocabularyUids)
                .query(query)
                .withFormat(withFormat != null && withFormat)
                .highlightPreTag(highlightPreTag)
                .highlightPostTag(highlightPostTag)
                .build();
    }
}
