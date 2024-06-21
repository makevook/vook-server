package vook.server.api.app.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.term.application.TermService;
import vook.server.api.app.contexts.term.domain.VocabularyId;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.domain.UserId;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RetrieveVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final TermService termService;

    public Result execute(Command command) {
        User user = userService.getByUid(command.userUid());
        List<Vocabulary> vocabularies = vocabularyService.findAllBy(new UserId(user.getId()));
        List<Result.Tuple> tupleList = vocabularies
                .stream()
                .map(vocabulary -> {
                    int termCount = termService.countByVocabularyId(new VocabularyId(vocabulary.getId()));
                    return Result.Tuple.from(vocabulary, termCount);
                }).toList();
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

            public static Tuple from(Vocabulary vocabulary, int termCount) {
                return new Tuple(
                        vocabulary.getUid(),
                        vocabulary.getName(),
                        termCount,
                        vocabulary.getCreatedAt()
                );
            }
        }
    }
}
