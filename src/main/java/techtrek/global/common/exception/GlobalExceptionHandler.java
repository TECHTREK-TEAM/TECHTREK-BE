package techtrek.global.common.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.response.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

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
                .status(code.getHttpStatus())
                .body(CommonResponse.from(code));
    }

    // 400에러 (형식 잘못)일때
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<?>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorCode code = ErrorCode.BAD_REQUEST;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(CommonResponse.from(code));
    }

    // 중복 데이터 존재할 경우
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<CommonResponse<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//        ErrorCode code = ErrorCode.ANALYSIS_DUPLICATE;
//        return ResponseEntity
//                .status(code.getHttpStatus())
//                .body(CommonResponse.from(code));
//    }

    // 이력서: Multipart 요청이 아닌 경우
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<CommonResponse<?>> handleMultipartException(MultipartException ex) {
        ErrorCode code = ErrorCode.BAD_MULTIPART;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(CommonResponse.from(code));
    }

    // 이력서: key값이 틀렸을 경우
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<CommonResponse<?>> handleMissingServletRequestPart(MissingServletRequestPartException ex) {
        ErrorCode code = ErrorCode.MISSING_FILE_PART;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(CommonResponse.from(code));
    }
}

