package vook.server.api.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RetrieveVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public Result execute(Command command) {
        User user = userService.getByUid(command.userUid());
        List<Vocabulary> vocabularies = vocabularyService.findAllBy(new UserId(user.getId()));
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
