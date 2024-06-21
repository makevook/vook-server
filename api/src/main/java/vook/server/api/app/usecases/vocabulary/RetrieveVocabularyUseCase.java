package vook.server.api.app.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RetrieveVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public Result execute(Command command) {
        User user = userService.findByUid(command.userUid()).orElseThrow();
        List<Vocabulary> vocabularies = vocabularyService.findAllBy(user);
        return Result.from(vocabularies);
    }


    public record Command(
            String userUid
    ) {
    }

    public record Result(
            List<Tuple> vocabularies
    ) {
        public static Result from(List<Vocabulary> vocabularies) {
            return new Result(vocabularies.stream().map(Tuple::from).toList());

        }

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
