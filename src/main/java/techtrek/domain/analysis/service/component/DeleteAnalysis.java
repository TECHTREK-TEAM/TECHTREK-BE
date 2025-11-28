package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteAnalysis {
    private final QuestionAnswerRepository questionAnswerRepository;
    private final AnalysisRepository analysisRepository;

    @Transactional
    public Boolean exec(Long analysisId, CustomUserDetails userDetails){
        // Analysis 조회
        Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new CustomException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 권한체크
        if (!analysis.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // 분석 테이블 삭제
        questionAnswerRepository.deleteByAnalysis(analysis);
        analysisRepository.deleteById(analysisId);


        return true;
    }
}
