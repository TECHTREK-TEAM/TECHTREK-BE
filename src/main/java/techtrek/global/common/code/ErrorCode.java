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
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다."), // ✅

    // 세션, 필드
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "세션을 찾을 수 없습니다."), // ✅
    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "필드를 찾을 수 없습니다."), // ✅
    PARENT_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "부모 필드를 찾을 수 없습니다."),
    PREVIOUS_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION404", "이전 필드를 찾을 수 없습니다."),

    // 질문
    ENTERPRISE_NOT_FOUND(HttpStatus.NOT_FOUND, "ENUM500", "해당 이름의 기업을 찾을 수 없습니다."), // ✅
    BASIC_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION500", "기본 질문을 찾을 수 없습니다."), // ✅
    ENUM_CS_KEYWORD_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "ENUM500", "ENUM CS 키워드를 찾을 수 없습니다."),

    // 이력서
    RESUME_NOT_FOUND(HttpStatus.BAD_REQUEST, "RESUME400", "이력서를 찾을 수 없습니다."),  // ✅
    BAD_MULTIPART(HttpStatus.BAD_REQUEST, "MULTIPART001", "파일 전송 형식이 올바르지 않습니다. multipart/form-data 형식으로 요청해주세요."),
    MISSING_FILE_PART(HttpStatus.BAD_REQUEST, "MULTIPART002", "요청에 필요한 키 이름인 'file'이 없습니다. 키 이름과 파일 첨부를 확인해주세요."),
    RESUME_PDF_PARSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RESUME500", "이력서 PDF 파싱에 실패했습니다."),

    // openAI
    PROMPT_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "프롬프트 txt 파일을 찾을 수 없습니다."), // ✅
    JSON_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PROMPT500", "JSON에서 객체로 변환하는데 실패하였습니다."), // ✅

    // 분석
    ANALYSIS_DUPLICATE(HttpStatus.CONFLICT, "DB409", "해당 세션에 이미 분석 데이터가 존재합니다. 중복 생성할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public boolean success() {
        return false;
    }

}
