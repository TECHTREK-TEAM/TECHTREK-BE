package techtrek.global.code.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(true, "COMMON200", "요청 성공"),

    BAD_REQUEST(false, "COMMON400", "잘못된 요청"),
    INTERNAL_SERVER_ERROR(false, "COMMON500", "서버 오류"),
    USER_NOT_FOUND(false, "USER404", "사용자를 찾을 수 없습니다"),
    DUPLICATE_USERNAME(false, "USER409", "중복된 사용자 이름입니다");

    private final boolean isSuccess;
    private final String code;
    private final String message;
}
