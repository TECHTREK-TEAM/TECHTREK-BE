package techtrek.global.exception;

import org.springframework.http.ResponseEntity;
import techtrek.global.code.CommonResponse;
import techtrek.global.code.status.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 만든 예외 처리
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CommonResponse<?>> handleGlobalException(GlobalException ex) {
        return new ResponseEntity<>(
                new CommonResponse<>(ex.getResponseCode(), null),
                ex.getResponseCode().getHttpStatus()
        );
    }

    // 그 외 예상하지 못한 에러
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception ex) {
        ex.printStackTrace(); // 로그 확인용
        return new CommonResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, null);
    }
}

