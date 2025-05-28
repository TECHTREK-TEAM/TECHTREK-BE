package techtrek.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ResponseCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "USER409", "중복된 사용자 이름입니다"),

    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "세션을 찾을 수 없습니다"),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다"),

    // redis
    REDIS_JSON_SERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS500", "JSON 직렬화를 실패하였습니다."),
    REDIS_JSON_DESERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS500", "JSON 역직렬화를 실패하였습니다."),

    // 질문
    PREVIOUS_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION500", "이전 질문을 찾을 수 없습니다"),
    PREVIOUS_QUESTION_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "이전 질문 데이터를 JSON에서 객체로 변환하는데 실패하였습니다."),
    BASIC_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION500", "기본 질문을 찾을 수 없습니다"),

    // 질문 - ENUM
    ENUM_ENTERPRISE_KEYWORD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "ENUM500", "ENUM 기업 키워드를 찾을 수 없습니다."),
    ENUM_CS_KEYWORD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "ENUM500", "ENUM CS 키워드를 찾을 수 없습니다."),

    // 이력서
    RESUME_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESUME400", "파일이 존재하지 않습니다"),
    RESUME_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RESUME500", "이력서 파싱에 실패했습니다"),

    // 프롬프트
    PROMPT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "프롬프트 txt 파일을 찾을 수 없습니다."),
    PROMPT_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "프롬프트 txt 파일을 읽을 수 없습니다."),
    PROMPT_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "프롬프트 JSON에서 객체로 변환하는데 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public boolean isSuccess() {
        return false;
    }

}
