package techtrek.domain.sessionInfo.service.bean.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.global.gpt.service.OpenAiService;

@Component
@RequiredArgsConstructor
public class CreateTailHelper {
    private final OpenAiService openAiService;

    public String exec(String parentQuestion, String parentAnswer){
        String prompt = String.format(
                "다음은 지원자가 이전에 면접에서 질문을 받고 답변을 한 내용입니다:\n" +
                        "질문: %s\n" +
                        "답변: %s\n\n" +
                        "이 내용을 바탕으로, 해당 지원자에게 적합한 **CS(Computer Science) 기반 기술 꼬리질문 1개만** 만들어 주세요.\n\n" +
                        "조건:\n" +
                        "- 반드시 CS 관련 주제여야 합니다 (예: 운영체제, 네트워크, 데이터베이스, 자료구조, 알고리즘, 트랜잭션, 멀티스레딩 등).\n" +
                        "- 일반적인 언어나 개념 질문은 피하고, 깊이 있는 구체적 질문이어야 합니다.\n" +
                        "- 질문은 3~4문장 이내로 간결하게 작성하세요.\n" +
                        "- 앞에 번호, 기호 등은 붙이지 마세요.\n" +
                        "- 이전에 나온 질문과 중복되지 않도록 해주세요.\n",
                parentQuestion,
                parentAnswer
        );

        // GPT에게 질문 생성 요청하는 코드에서 사용
        String question = openAiService.askToGpt(prompt);

        return question;
    }
}
