package vook.server.api.web.routes.glossary;

import lombok.Getter;
import vook.server.api.model.Glossary;

import java.util.List;

@Getter
public class RetrieveResponse {

    private String uid;
    private String name;

    public static List<RetrieveResponse> from(List<Glossary> glossaries) {
        return glossaries.stream()
                .map(RetrieveResponse::from)
                .toList();
    }

    public static RetrieveResponse from(Glossary glossary) {
        RetrieveResponse response = new RetrieveResponse();
        response.uid = glossary.getUid();
        response.name = glossary.getName();
        return response;
    }
}
