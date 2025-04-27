package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;

@Getter
@Setter
@AllArgsConstructor
public class SessionInfoRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        private String enterpriseName;
        private EnterpriseType enterpriseType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Answer {
        private String sessionId;
        private String fieldId;
        private String answer;
    }
}
