package techtrek.global.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.response.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(2)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 만든 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<?>> handleCustomException(CustomException ex) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(CommonResponse.from(code));
    }


    // 그 외 예상하지 못한 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGeneralException(Exception ex) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(Integer.parseInt(code.getCode()))
                .body(CommonResponse.from(code));
    }
}

