package vook.server.api.domain.vocabulary.logic.term;

import lombok.Builder;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.term.TermFactory;

import java.util.List;

@Builder
public record TermCreateAllCommand(
        String vocabularyUid,
        List<TermInfo> termInfos
) {

    public List<Term> toEntity(TermFactory termFactory) {
        return termFactory.createForBatchCreate(new TermFactory.CreateForBatchCommand(
                vocabularyUid,
                termInfos.stream()
                        .map(termInfo -> new TermFactory.TermInfo(termInfo.term(), termInfo.meaning(), termInfo.synonyms()))
                        .toList()
        ));
    }

    @Builder
    public record TermInfo(
            String term,
            String meaning,
            List<String> synonyms
    ) {
    }
}
