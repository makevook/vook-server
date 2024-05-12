package vook.server.api.outbound.search;

import vook.server.api.model.Glossary;
import vook.server.api.model.Term;

import java.util.List;

public interface SearchService {
    void createGlossary(Glossary glossary);

    void addTerms(List<Term> terms, Glossary glossary);

    SearchResult search(SearchParams params);
}
