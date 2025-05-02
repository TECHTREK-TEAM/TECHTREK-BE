package techtrek.global.code;

import org.springframework.http.ResponseEntity;
import techtrek.global.code.status.ResponseCode;


public class ApiResponse {

    // 성공 응답을 처리하는 메서드
    public static <T> ResponseEntity<CommonResponse<T>> onSuccess(T data) {
        return ResponseEntity.ok(new CommonResponse<>(ResponseCode.SUCCESS, data));
    }

    // 실패 응답을 처리하는 메서드
    public static <T> ResponseEntity<CommonResponse<T>> onError(ResponseCode responseCode) {
        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(new CommonResponse<>(responseCode, null));
    }
}


