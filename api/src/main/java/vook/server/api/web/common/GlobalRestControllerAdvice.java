package vook.server.api.web.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(CommonApiException.Exception.class)
    public ResponseEntity<?> handleCommonApiException(CommonApiException.Exception e) {
        log.error(e.getMessage(), e);

        CommonApiResponse<?> response = e.response();

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error(e.getMessage(), e);

        CommonApiResponse<?> response = new CommonApiException
                .ServerError("처리되지 않은 서버 에러가 발생하였습니다.", e)
                .response();

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
