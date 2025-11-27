package techtrek.domain.session.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class SessionRedisHelper {

    private final RedisTemplate<String, String> redisTemplate;
    private final EnterpriseRepository enterpriseRepository;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.qa}")
    private String qaPrefix;


    // 키 예외처리
    public void validateSession(String sessionKey) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }
    }

    // redis 값 조회
    public int getIntField(String key, String field) {
        Object value = redisTemplate.opsForHash().get(key, field);
        if (value == null) throw new CustomException(ErrorCode.FIELD_NOT_FOUND);
        return Integer.parseInt(value.toString());
    }

    // enterprise 조회, 예외처리
    public Enterprise getEnterprise(String sessionKey) {
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        return enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));
    }

    // QA키 생성
    public String buildQaKey(String sessionId, int main, int sub) {
        String base = interviewPrefix + sessionId + qaPrefix + main;
        return (sub > 0) ? base + ":" + sub : base;
    }
}

