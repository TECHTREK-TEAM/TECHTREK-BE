package techtrek.global.common.response;

import org.springframework.http.ResponseEntity;
import techtrek.global.common.code.SuccessCode;


public class ApiResponse {

    // 성공 응답을 처리하는 메서드
    public static <T> ResponseEntity<CommonResponse<T>> onSuccess(T data) {
        return ResponseEntity.ok(new CommonResponse<>(SuccessCode.SUCCESS, data));
    }

    // 실패 응답을 처리하는 메서드
//    public static <T> ResponseEntity<CommonResponse<T>> onError(ErrorCode errorCode) {
//        return ResponseEntity.status(errorCode.getHttpStatus())
//                .body(new CommonResponse<>(errorCode, null));
//    }
}


