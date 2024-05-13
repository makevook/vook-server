package vook.server.api.web.routes.demo.reqres;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.model.demo.DemoTermSynonym;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RetrieveTermsResponse {

    private String term;

    private List<String> synonyms;

    private String meaning;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String createdBy;

    public static List<RetrieveTermsResponse> from(List<DemoTerm> terms) {
        return terms.stream()
                .map(RetrieveTermsResponse::from)
                .toList();
    }

    public static RetrieveTermsResponse from(DemoTerm term) {
        RetrieveTermsResponse response = new RetrieveTermsResponse();
        response.term = term.getTerm();
        response.synonyms = term.getSynonyms().stream().map(DemoTermSynonym::getSynonym).toList();
        response.meaning = term.getMeaning();
        response.createdAt = term.getCreatedAt();
        response.createdBy = "vook";
        return response;
    }
}
