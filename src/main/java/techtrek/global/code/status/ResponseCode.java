package techtrek.global.code.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(HttpStatus.OK, "COMMON200", "요청 성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "USER409", "중복된 사용자 이름입니다"),
    BASIC_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION404", "기본 질문을 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public boolean isSuccess() {
        return this.httpStatus.is2xxSuccessful();
    }

}
