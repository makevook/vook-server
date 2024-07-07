package vook.server.api.web.term.reqres;

import lombok.Builder;
import vook.server.api.usecases.term.CreateTermUseCase;

@Builder
public record TermCreateResponse(
        String uid
) {
    public static TermCreateResponse from(CreateTermUseCase.Result term) {
        return TermCreateResponse.builder()
                .uid(term.uid())
                .build();
    }
}
