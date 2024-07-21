package vook.server.api.domain.vocabulary.logic.term;

import lombok.Builder;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.term.TermFactory;

import java.util.List;

@Builder
public record TermUpdateCommand(
        String uid,
        String term,
        String meaning,
        List<String> synonyms
) {

    public Term toEntity(TermFactory termFactory) {
        return termFactory.createForUpdate(
                new TermFactory.UpdateCommand(new TermFactory.TermInfo(term, meaning, synonyms))
        );
    }
}
