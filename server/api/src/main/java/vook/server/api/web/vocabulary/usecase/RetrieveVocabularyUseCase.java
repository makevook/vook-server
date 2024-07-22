package vook.server.api.web.vocabulary.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.vocabulary.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.globalcommon.annotation.UseCase;

import java.time.LocalDateTime;
import java.util.List;

@UseCase
@RequiredArgsConstructor
public class RetrieveVocabularyUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;

    public Result execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        List<Vocabulary> vocabularies = vocabularyLogic.findAllBy(new UserUid(command.userUid()));
        List<Result.Tuple> tupleList = vocabularies
                .stream()
                .map(Result.Tuple::from)
                .toList();
        return new Result(tupleList);
    }

    public record Command(
            String userUid
    ) {
    }

    public record Result(
            List<Tuple> vocabularies
    ) {
        public record Tuple(
                String uid,
                String name,
                Integer termCount,
                LocalDateTime createdAt
        ) {

            public static Tuple from(Vocabulary vocabulary) {
                return new Tuple(
                        vocabulary.getUid(),
                        vocabulary.getName(),
                        vocabulary.termCount(),
                        vocabulary.getCreatedAt()
                );
            }
        }
    }
}
