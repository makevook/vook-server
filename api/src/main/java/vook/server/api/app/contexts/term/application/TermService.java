package vook.server.api.app.contexts.term.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.term.domain.TermRepository;
import vook.server.api.app.contexts.term.exception.TermLimitExceededException;
import vook.server.api.app.contexts.term.application.data.TermCreateCommand;

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
