package vook.server.api.domain.vocabulary.model.vocabulary;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface VocabularyFactory {

    Vocabulary create(@NotEmpty String name, @NotNull UserUid userUid);
}
