package techtrek.global.common.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
    boolean isSuccess();
}

