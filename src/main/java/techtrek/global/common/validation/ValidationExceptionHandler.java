package techtrek.global.common.validation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import techtrek.global.common.response.ValidationResponse;

@Order(1)
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        // 모든 필드 에러 메시지를 연결
        StringBuilder errorMessages = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessages.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }

        // 마지막 세미콜론과 공백 제거
        if (errorMessages.length() > 0) {
            errorMessages.setLength(errorMessages.length() - 2);
        }

        ValidationResponse validationResponse = ValidationResponse.builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessages.toString())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResponse);
    }

}
