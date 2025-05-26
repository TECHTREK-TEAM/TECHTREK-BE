package techtrek.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import techtrek.global.common.code.ResponseCode;

@JsonPropertyOrder({ "isSuccess", "code", "message", "data" })
@Getter
public class CommonResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;

    private final String code;
    private final String message;
    private final T data;

    @JsonIgnore
    public boolean getSuccess() {
        return isSuccess;
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
