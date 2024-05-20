package vook.server.api.web.routes.user;

import lombok.Getter;
import vook.server.api.model.terms.Terms;

import java.util.List;

@Getter
public class UserTermsResponse {

    private Long id;
    private String title;
    private String content;
    private Long version;

    public static List<UserTermsResponse> from(List<Terms> terms) {
        return terms.stream()
                .map(UserTermsResponse::from)
                .toList();
    }

    public static UserTermsResponse from(Terms terms) {
        UserTermsResponse response = new UserTermsResponse();
        response.id = terms.getId();
        response.title = terms.getTitle();
        response.content = terms.getContent();
        response.version = terms.getVersion();
        return response;
    }
}
