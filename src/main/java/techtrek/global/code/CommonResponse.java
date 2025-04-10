package techtrek.global.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import techtrek.global.code.status.ResponseCode;

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

    public CommonResponse(ResponseCode code, T data) {
        this.isSuccess = code.isSuccess();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data;
    }

}
