package vook.server.api.usecases.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.service.VocabularyService;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawUserUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        User user = userService.getCompletedUserByUid(command.userUid());

        userService.withdraw(user.getUid());

        vocabularyService.findAllBy(new UserId(user.getId())).forEach(v -> {
            vocabularyService.delete(v.getUid());
        });
    }

    public record Command(
            String userUid
    ) {
    }
}
