package vook.server.api.app.domain.vocabulary.exception;

import vook.server.api.app.common.AppException;

public class VocabularyLimitExceededException extends AppException {
    @Override
    public String contents() {
        return "VocabularyLimitExceeded";
    }
}
