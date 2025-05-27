package techtrek.domain.sessionInfo.service.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.OpenAiService;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final UserRepository userRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionRepository basicQuestionRepository;
    private final OpenAiService openAiService;

    public SessionInfoResponse.NewQuestion exec(String sessionId){
        // User user = userRepository.findById(userId)
        User user = userRepository.findById("1")
                .orElseThrow(() ->  new CustomException(ErrorCode.USER_NOT_FOUND));
        // 1. 기본 설정
        String sessionKey = "interview:session:" + sessionId;
        String fieldId = UUID.randomUUID().toString();

        // 2. Redis에서 최근 질문 가져오기 (가장 최근 질문을 가져오기 위해 리스트 끝에서 하나를 꺼냄)
        List<String> jsonStrings = redisTemplate.opsForList().range(sessionKey + ":new", -1, -1);
        String lastJsonString = jsonStrings.isEmpty() ? null : jsonStrings.get(0);

        // 초기화
        String question = "";
        String phase;
        int count = 0;

        // 2. Redis에서 최근 질문 가져오기
        if (lastJsonString != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> lastQaData = objectMapper.readValue(lastJsonString, Map.class);

                // 이전 phase와 count 가져오기
                phase = lastQaData.get("phase");
                count = Integer.parseInt(lastQaData.get("count"));

                // count가 5 이상이면 phase 전환
                if (count >= 5) {
                    phase = phase.equals("basic") ? "resume" : "basic";
                    count = 0;
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 파싱 실패", e);
            }
        } else {
            // 첫 질문인 경우
            phase = "basic";
            count = 0;
        }

        // phase에 따른 질문 설정
        if (phase.equals("basic")) {
            // 2-0. 기업이름 불러오기
            SessionInfo sessionInfo = sessionInfoRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

            EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

            // 2-1. 기업의 키워드 목록 가져오기
            List<String> keywords = enterpriseName.getKeywords();
            if (keywords.isEmpty()) {
                throw new CustomException(ErrorCode.ENUM_ENTERPRISE_KEYWORD_NOT_FOUND);
            }

            // 2-2. 랜덤 키워드 선택
            String selectedKeyword = keywords.get(new Random().nextInt(keywords.size()));

            // 2-3. 키워드를 이용해 questionCategory 찾기
            CSCategory category = CSCategory.fromKeyword(selectedKeyword)
                    .orElseThrow(() -> new CustomException(ErrorCode.ENUM_CS_KEYWORD_NOT_FOUND));

            // 2-4. 해당 카테고리 질문 리스트 조회
            List<BasicQuestion> questions = basicQuestionRepository.findByCSCategory(category);
            if (questions == null || questions.isEmpty()) {
                throw new CustomException(ErrorCode.BASIC_QUESTION_NOT_FOUND);
            }


            // 2-5. 질문 랜덤 선택
            BasicQuestion randomQuestion = questions.get(new Random().nextInt(questions.size()));

            // 2-6 최종 질문 텍스트
            question = randomQuestion.getQuestion();

        } else {
            SessionInfo sessionInfo = sessionInfoRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

            String resume = user.getResume(); // 사용자 이력서
            String enterpriseDescription = sessionInfo.getEnterpriseName().getDescription(); // 기업 설명

            String prompt = String.format(
                    "다음은 지원자의 이력서입니다:\n%s\n\n" +
                            "해당 지원자는 \"%s\" 성향의 기업 면접을 준비 중입니다.\n" +
                            "이력서를 기반으로 한 **정확하고 구체적인 CS(Computer Science) 기반 기술 면접 질문 1개만** 생성해 주세요.\n\n" +
                            "조건:\n" +
                            "- '언어의 장단점'처럼 일반적인 질문은 피하세요.\n" +
                            "- 반드시 CS 기반 주제 (예: 운영체제, 네트워크, DB, 자료구조, 알고리즘, 트랜잭션, 멀티스레딩 등)여야 합니다.\n" +
                            "- 이전에 생성된 질문과 겹치지 않아야 합니다. 또한 질문의 길이는 3-4문장 이내로 작성하세요.\n" +
                            "- 앞에 번호, 기호 등은 붙이지 말고 **이력서를 보아하니, 이력서를 보니, 확인해본 결과 등의 문장을 섞어,** 질문해주세요. \n" +
                            "- 연속으로 **이력서를 보아하니, 이력서를 보니, 확인해본 결과**의 문장은 사용하지 마세요. \n" +
                            "- 너무 포괄적이지 않고 면접관이 깊이 파고들 수 있는 질문 형태로 출력하세요.\n\n",
                    resume,
                    enterpriseDescription
            );

            // GPT에게 질문 생성 요청하는 코드에서 사용
            question = openAiService.askToGpt(prompt);
        }

        // 3. 기본 + 이력서 질문 번호 계산
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 4. 전체 질문 개수 계산 (기본 + 이력서 + 꼬리질문 포함)
        Long currentTotalCount = redisTemplate.opsForList().size(sessionKey + ":new") + redisTemplate.opsForList().size(sessionKey + ":tail") ;
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 5. 묶기
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> qaData = new HashMap<>();
        qaData.put("fieldId", fieldId);
        qaData.put("question", question);
        qaData.put("answer", "");
        qaData.put("questionNumber", questionNumber);
        qaData.put("count", String.valueOf(count + 1));
        qaData.put("phase", phase);
        qaData.put("totalQuestionCount", totalQuestionCount);
        qaData.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });

        // 6. JSON 문자열로 변환
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(qaData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패", e);
        }

        // 7. Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);


        // 8. 기본 질문 반환
        return new SessionInfoResponse.NewQuestion(fieldId, question, questionNumber);

    }
}
