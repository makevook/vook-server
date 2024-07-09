package vook.server.api.domain.vocabulary.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateVocabularyType {

    // 개발 용어집
    DEVELOPMENT("개발: 비개발자를 위한 개발 용어집"),

    // 마케팅 용어집
    MARKETING("마케팅: 마케팅 실무 용어집"),

    // 디자인 용어집
    DESIGN("디자인: 개발자를 위한 디자인 용어집"),

    // 일반 사무 용어집
    GENERAL_OFFICE("실무: IT 실무 용어집");

    private final String vocabularyName;
}
