package techtrek.global.common.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import techtrek.global.common.code.ResponseCode;

@Getter
@JsonPropertyOrder({ "isSuccess", "code", "message", "data" })
public class CommonResponse<T> {

    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;

    @JsonGetter("isSuccess")
    public boolean isSuccess() {
        return isSuccess;
    }

    @Builder
    public CommonResponse(boolean isSuccess, String code, String message, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.isSuccess = responseCode.isSuccess();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public static <T> CommonResponse<T> from(ResponseCode code) {
        return new CommonResponse<>(code, null);
    }
}