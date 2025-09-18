package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteAnalysis {
    private final RedisTemplate<String, String> redisTemplate;
    private final AnalysisRepository analysisRepository;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(Long analysisId, CustomUserDetails userDetails){
        // Analysis 조회
        Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new CustomException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 권한체크
        if (!analysis.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // redis 삭제
        Set<String> keys = redisTemplate.keys(interviewPrefix + analysis.getSessionId() + "*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);

        // 분석 테이블 삭제
        analysisRepository.deleteById(analysisId);


        return true;
    }
}
