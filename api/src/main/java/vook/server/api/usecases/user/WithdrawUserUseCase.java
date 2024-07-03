package vook.server.api.usecases.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.service.VocabularyService;

@Service
@Transactional
@RequiredArgsConstructor
public class WithdrawUserUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        userService.validateCompletedUserByUid(command.userUid());

        userService.withdraw(command.userUid());

        vocabularyService.findAllBy(new UserUid(command.userUid())).forEach(v -> {
            vocabularyService.delete(v.getUid());
        });
    }

    public record Command(
            String userUid
    ) {
    }
}
