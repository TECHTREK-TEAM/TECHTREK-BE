package techtrek.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements ResponseCode {
    SUCCESS(HttpStatus.OK, "COMMON200", "요청 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public boolean isSuccess() {
        return true;
    }
}
