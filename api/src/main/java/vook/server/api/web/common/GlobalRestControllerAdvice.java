package vook.server.api.web.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(CommonApiException.Exception.class)
    public ResponseEntity<?> handleCommonApiException(CommonApiException.Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.statusCode()).body(e.response());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        CommonApiException.BadRequest badRequest = new CommonApiException.BadRequest(ApiResponseCode.BadRequest.INVALID_PARAMETER, e);
        return ResponseEntity.status(badRequest.statusCode()).body(badRequest.response());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        CommonApiException.ServerError serverError = new CommonApiException.ServerError(ApiResponseCode.ServerError.UNHANDLED_ERROR, e);
        return ResponseEntity.status(serverError.statusCode()).body(serverError.response());
    }
}
