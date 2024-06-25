package vook.server.api.usecases.term;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermSynonym;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RetrieveTermUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermSearchService termSearchService;

    public Result execute(Command command) {
        User user = userService.getByUid(command.userUid());
        Vocabulary vocabulary = vocabularyService.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(user, vocabulary);

        Page<Term> termPage = termSearchService.findAllBy(command.pageable());
        Page<Result.Tuple> tuplePage = termPage.map(Result.Tuple::from);
        return new Result(tuplePage);
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            Pageable pageable
    ) {
    }

    public record Result(
            Page<Tuple> terms
    ) {
        public record Tuple(
                String termUid,
                String term,
                String meaning,
                List<String> synonym,
                LocalDateTime createdAt
        ) {

            public static Tuple from(Term term) {
                return new Tuple(
                        term.getUid(),
                        term.getTerm(),
                        term.getMeaning(),
                        term.getSynonyms().stream().map(TermSynonym::getSynonym).toList(),
                        term.getCreatedAt()
                );
            }
        }
    }

    public interface TermSearchService {
        Page<Term> findAllBy(Pageable params);
    }
}
