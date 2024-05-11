package vook.server.api.web.routes.glossary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vook.server.api.model.Glossary;
import vook.server.api.model.Term;
import vook.server.api.model.TermSynonym;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FindAllTermsResponse {

    private String term;

    private List<String> synonyms;

    private String meaning;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String createdBy;

    public static List<FindAllTermsResponse> from(Glossary glossary, List<Term> terms) {
        return terms.stream()
                .map(term -> from(glossary, term))
                .toList();
    }

    public static FindAllTermsResponse from(Glossary glossary, Term term) {
        FindAllTermsResponse response = new FindAllTermsResponse();
        response.term = term.getTerm();
        response.synonyms = term.getSynonyms().stream().map(TermSynonym::getSynonym).toList();
        response.meaning = term.getMeaning();
        response.createdAt = term.getCreatedAt();
        response.createdBy = glossary.getMember().getName();
        return response;
    }
}
