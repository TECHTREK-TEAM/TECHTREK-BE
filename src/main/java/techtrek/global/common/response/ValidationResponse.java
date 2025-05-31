package techtrek.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({ "success", "code", "message" })
public class ValidationResponse {
    private boolean success;
    private int code;
    private String message;

}
