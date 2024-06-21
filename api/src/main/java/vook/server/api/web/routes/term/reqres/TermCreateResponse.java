package vook.server.api.web.routes.term.reqres;

import lombok.Getter;
import vook.server.api.model.term.Term;

@Getter
public class TermCreateResponse {

    private String uid;

    public static TermCreateResponse from(Term term) {
        TermCreateResponse response = new TermCreateResponse();
        response.uid = term.getUid();
        return response;
    }
}
