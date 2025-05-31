package techtrek.global.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ValidationResponse {
    private int status;
    private String message;
    private Map<String, String> errors; // 필드별 에러 메시지
}
