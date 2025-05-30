package techtrek.global.common.validation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import techtrek.global.common.response.CommonResponse;

import java.util.List;
import java.util.stream.Collectors;

@Order(1)
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        CommonResponse commonResponse = CommonResponse. builder()
                .isSuccess(false)
                .code("400")
                .message(String.join(", ", errors))
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }
}
