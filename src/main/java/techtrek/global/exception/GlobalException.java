package techtrek.global.exception;


import techtrek.global.code.status.ResponseCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final ResponseCode responseCode;

    public GlobalException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}

