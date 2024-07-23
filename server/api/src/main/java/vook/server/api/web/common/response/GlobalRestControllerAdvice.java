package vook.server.api.web.common.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vook.server.api.globalcommon.exception.AppException;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException e) {
        log.debug(e.getMessage(), e);
        String contents = e.getClass().getSimpleName().replace("Exception", "");
        CommonApiException badRequest = CommonApiException.badRequest(ApiResponseCode.BadRequest.VIOLATION_BUSINESS_RULE, e, contents);
        return ResponseEntity.status(badRequest.statusCode()).body(badRequest.response());
    }

    @ExceptionHandler(CommonApiException.class)
    public ResponseEntity<?> handleCommonApiException(CommonApiException e) {
        log.debug(e.getMessage(), e);
        return ResponseEntity.status(e.statusCode()).body(e.response());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug(e.getMessage(), e);
        CommonApiException badRequest = CommonApiException.badRequest(ApiResponseCode.BadRequest.INVALID_PARAMETER, e);
        return ResponseEntity.status(badRequest.statusCode()).body(badRequest.response());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.debug(e.getMessage(), e);
        CommonApiException badRequest = CommonApiException.badRequest(ApiResponseCode.BadRequest.INVALID_PARAMETER, e);
        return ResponseEntity.status(badRequest.statusCode()).body(badRequest.response());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        CommonApiException serverError = CommonApiException.serverError(ApiResponseCode.ServerError.UNHANDLED_ERROR, e);
        return ResponseEntity.status(serverError.statusCode()).body(serverError.response());
    }
}
