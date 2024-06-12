package vook.server.api.app.vocabulary.exception;

import vook.server.api.app.common.AppException;

public class VocabularyNotFoundException extends AppException {
    @Override
    public String contents() {
        return "VocabularyNotFound";
    }
}
