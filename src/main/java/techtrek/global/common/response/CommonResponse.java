package techtrek.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import techtrek.global.common.code.ResponseCode;

@Getter
@JsonPropertyOrder({ "success", "code", "message", "data" })
public class CommonResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;


    @Builder
    public CommonResponse(boolean isSuccess, String code, String message, T data) {
        this.success = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.success = responseCode.success();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public static <T> CommonResponse<T> from(ResponseCode code) {
        return new CommonResponse<>(code, null);
    }

}