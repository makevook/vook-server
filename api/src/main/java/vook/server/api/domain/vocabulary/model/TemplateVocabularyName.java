package vook.server.api.domain.vocabulary.model;

import vook.server.api.domain.user.model.Job;

public enum TemplateVocabularyName {
    // 개발 용어집
    DEVELOPMENT,

    // 마케팅 용어집
    MARKETING,

    // 디자인 용어집
    DESIGN,

    // 일반 사무 용어집
    GENERAL_OFFICE;

    public static TemplateVocabularyName from(Job job) {
        return switch (job) {
            case PLANNER, DESIGNER -> DEVELOPMENT;
            case MARKETER -> MARKETING;
            case DEVELOPER -> DESIGN;
            case CEO, HR, OTHER -> GENERAL_OFFICE;
            case null -> GENERAL_OFFICE;
        };
    }
}
