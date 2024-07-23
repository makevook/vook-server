package vook.server.api.domain.vocabulary.model.vocabulary;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.globalcommon.annotation.ModelFactory;

import java.util.UUID;

@ModelFactory
@RequiredArgsConstructor
public class DefaultVocabularyFactory implements VocabularyFactory {

    private final VocabularyRepository repository;

    @Override
    public Vocabulary create(@NotEmpty String name, @NotNull UserUid userUid) {
        if (repository.findAllByUserUid(userUid).size() >= 3) {
            throw new VocabularyLimitExceededException();
        }

        return Vocabulary.builder()
                .uid(UUID.randomUUID().toString())
                .name(name)
                .userUid(userUid)
                .build();
    }
}
