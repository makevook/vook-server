package vook.server.api.web.routes.term.reqres;

import lombok.Getter;
import vook.server.api.app.usecases.term.CreateTermUseCase;

@Getter
public class TermCreateResponse {

    private String uid;

    public static TermCreateResponse from(CreateTermUseCase.Result term) {
        TermCreateResponse response = new TermCreateResponse();
        response.uid = term.uid();
        return response;
    }
}
