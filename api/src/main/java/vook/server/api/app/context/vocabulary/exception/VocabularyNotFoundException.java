package vook.server.api.app.context.vocabulary.exception;

import vook.server.api.common.exception.AppException;

public class VocabularyNotFoundException extends AppException {
    @Override
    public String contents() {
        return "VocabularyNotFound";
    }
}
