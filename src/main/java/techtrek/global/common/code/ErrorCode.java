package techtrek.global.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ResponseCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "입력값 형식이 잘못되었습니다. 다시 확인해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 오류"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "USER409", "중복된 사용자 이름입니다."),

    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다."), //ok
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "AUTH403", "해당 세션에 대한 권한이 없습니다."),

    // 세션, 필드
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "세션을 찾을 수 없습니다."), // ✅
    QA_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "필드를 찾을 수 없습니다."), // ✅
    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "필드를 찾을 수 없습니다."), // ✅
    PARENT_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "부모 필드를 찾을 수 없습니다."), // ✅
    PREVIOUS_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "이전 필드를 찾을 수 없습니다."), // ✅

    // 질문
    ENTERPRISE_NOT_FOUND(HttpStatus.NOT_FOUND, "ENUM500", "해당 이름의 기업을 찾을 수 없습니다."), // ✅
    BASIC_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION500", "기본 질문을 찾을 수 없습니다."), // ✅

    // 이력서
    RESUME_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESUME400", "이력서를 찾을 수 없습니다."),  // ✅
    BAD_MULTIPART(HttpStatus.BAD_REQUEST, "MULTIPART001", "파일 전송 형식이 올바르지 않습니다. multipart/form-data 형식으로 요청해주세요."), // ✅
    MISSING_FILE_PART(HttpStatus.BAD_REQUEST, "MULTIPART002", "요청에 필요한 키 이름인 'file'이 없습니다. 키 이름과 파일 첨부를 확인해주세요."), // ✅
    RESUME_PDF_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RESUME500", "이력서 PDF 파싱에 실패했습니다."),

    // openAI
    PROMPT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "프롬프트 txt 파일을 찾을 수 없습니다."), // ✅
    JSON_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "JSON에서 객체로 변환하는데 실패하였습니다."), // ✅

    // 분석
    ANALYSIS_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESUME400", "분석 정보를 찾을 수 없습니다."), // ✅
    INVALID_SIMILARITY_VALUE(HttpStatus.BAD_REQUEST, "ANALYSIS401", "유효하지 않은 similarity 값입니다."),  // ✅
    REDIS_HASH_ACCESS_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS500", "Redis 해시 조회 중 오류가 발생했습니다."),  // ✅

    // 소셜 로그인
    OAUTH_PROVIDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "OAUTH400", "지원하지 않는 OAuth 제공자입니다."), // 400
    OAUTH_TOKEN_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH500", "토큰 요청에 실패했습니다."), // 500
    OAUTH_USERINFO_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "OAUTH501", "사용자 정보를 가져오는데 실패했습니다."), // 500
    LOGIN_ALREADY_EXISTS(HttpStatus.CONFLICT, "OAUTH409", "이미 다른 로그인 방식으로 가입된 회원입니다."); // 409

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public boolean success() {
        return false;
    }

}
