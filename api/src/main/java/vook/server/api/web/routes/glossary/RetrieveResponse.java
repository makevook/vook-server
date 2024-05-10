package vook.server.api.web.routes.glossary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import vook.server.api.model.Glossary;

import java.util.List;

@Getter
public class RetrieveResponse {

    @Schema(description = "용어집 UID", examples = "38617d11-1d8e-4f77-a2fd-8cdca9de8420")
    private String uid;
    @Schema(description = "용어집 이름", examples = "실무")
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
