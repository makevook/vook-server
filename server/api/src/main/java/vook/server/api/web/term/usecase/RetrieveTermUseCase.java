package vook.server.api.web.term.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.vocabulary.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

import java.time.LocalDateTime;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class RetrieveTermUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;
    private final VocabularyPolicy vocabularyPolicy;
    private final TermSearchService termSearchService;

    public Result execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Vocabulary vocabulary = vocabularyLogic.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(command.userUid(), vocabulary);

        Page<Term> termPage = termSearchService.findAllBy(command.vocabularyUid(), command.pageable());
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
                List<String> synonyms,
                LocalDateTime createdAt
        ) {

            public static Tuple from(Term term) {
                return new Tuple(
                        term.getUid(),
                        term.getTerm(),
                        term.getMeaning(),
                        term.getSynonyms(),
                        term.getCreatedAt()
                );
            }
        }
    }

    public interface TermSearchService {
        Page<Term> findAllBy(String vocabularyUid, Pageable params);
    }
}
