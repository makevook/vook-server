package vook.server.api.app.contexts.vocabulary.exception;

import vook.server.api.app.common.exception.AppException;

public class VocabularyNotFoundException extends AppException {
    @Override
    public String contents() {
        return "VocabularyNotFound";
    }
}
