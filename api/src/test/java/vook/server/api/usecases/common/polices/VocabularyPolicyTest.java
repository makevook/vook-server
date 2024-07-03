package vook.server.api.usecases.common.polices;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VocabularyPolicyTest {

    private final VocabularyPolicy vocabularyPolicy = new VocabularyPolicy();

    @Test
    @DisplayName("소유권 검증 - 성공")
    void validateOwnerSuccess() {
        List<String> userVocabularyUids = List.of(
                "245d8a9a-6272-4b8c-8daa-0f79bdc78495",
                "004b83a6-2b47-45f4-8d99-3299a8f1a055"
        );

        List<String> targetVocabularyUids = List.of(
                "245d8a9a-6272-4b8c-8daa-0f79bdc78495",
                "004b83a6-2b47-45f4-8d99-3299a8f1a055"
        );

        vocabularyPolicy.validateOwner(userVocabularyUids, targetVocabularyUids);
    }

    @Test
    @DisplayName("소유권 검증 - 실패; 유저의 단어장 소유권과 대상 단어장 소유권의 ID가 1자만 다를 경우에도 예외를 발생시킨다.")
    void validateOwner() {
        List<String> userVocabularyUids = List.of(
                "245d8a9a-6272-4b8c-8daa-0f79bdc78495",
                "004b83a6-2b47-45f4-8d99-3299a8f1a055"
        );

        List<String> targetVocabularyUids = List.of(
                "245d8a9a-6272-4b8c-8daa-0f79bdc78495",
                "004b83a6-2b47-45f4-8d99-3299a8f1a054"
        );

        assertThatThrownBy(() -> vocabularyPolicy.validateOwner(userVocabularyUids, targetVocabularyUids))
                .isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }
}
