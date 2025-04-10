package techtrek.global.exception;

import techtrek.global.code.CommonResponse;
import techtrek.global.code.status.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 만든 예외 처리
    @ExceptionHandler(GlobalException.class)
    public CommonResponse<?> handleGlobalException(GlobalException ex) {
        return new CommonResponse<>(ex.getResponseCode(), null);
    }

    // 그 외 예상하지 못한 에러
    @ExceptionHandler(Exception.class)
    public CommonResponse<?> handleException(Exception ex) {
        ex.printStackTrace(); // 로그 확인용
        return new CommonResponse<>(ResponseCode.INTERNAL_SERVER_ERROR, null);
    }
}

