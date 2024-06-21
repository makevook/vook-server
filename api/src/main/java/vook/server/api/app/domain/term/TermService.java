package vook.server.api.app.domain.term;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.domain.term.data.TermCreateCommand;
import vook.server.api.app.domain.term.exception.TermLimitExceededException;
import vook.server.api.app.domain.term.model.TermRepository;
import vook.server.api.app.domain.term.model.Term;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    public Term create(TermCreateCommand command) {
        int termCount = command.getVocabulary().termCount();
        if (termCount >= 100) {
            throw new TermLimitExceededException();
        }

        Term saved = termRepository.save(command.toEntity());
        saved.addAllSynonym(command.getSynonyms());
        return saved;
    }
}
