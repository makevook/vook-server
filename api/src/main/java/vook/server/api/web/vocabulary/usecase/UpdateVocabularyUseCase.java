package vook.server.api.web.vocabulary.usecase;

import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.VocabularyUpdateCommand;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.globalcommon.annotation.UseCase;
import vook.server.api.policy.VocabularyPolicy;

@UseCase
@RequiredArgsConstructor
public class UpdateVocabularyUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;
    private final VocabularyPolicy vocabularyPolicy;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        Vocabulary vocabulary = vocabularyLogic.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(command.userUid(), vocabulary);

        vocabularyLogic.update(command.toServiceCommand());
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            String name
    ) {
        public VocabularyUpdateCommand toServiceCommand() {
            return VocabularyUpdateCommand
                    .builder()
                    .vocabularyUid(vocabularyUid)
                    .name(name)
                    .build();
        }
    }
}
