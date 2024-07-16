package vook.server.api.web.user.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.globalcommon.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class WithdrawUserUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        userLogic.withdraw(command.userUid());

        vocabularyLogic.findAllBy(new UserUid(command.userUid())).forEach(v -> {
            vocabularyLogic.delete(v.getUid());
        });
    }

    public record Command(
            String userUid
    ) {
    }
}
