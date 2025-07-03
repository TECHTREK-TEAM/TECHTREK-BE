package techtrek.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import techtrek.global.common.code.ResponseCode;

@Getter
@JsonPropertyOrder({ "success", "code", "message", "data" })
public class CommonResponse<T> {
    @Schema(description = "응답 성공 여부", example = "true")
    private final boolean success;

    @Schema(description = "응답 코드", example = "COMMON200")
    private final String code;

    @Schema(description = "응답 메시지", example = "요청 성공")
    private final String message;

    @Schema(description = "응답 데이터")
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