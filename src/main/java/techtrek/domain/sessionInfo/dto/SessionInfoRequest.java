package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;

@Getter
@Setter
@AllArgsConstructor
public class SessionInfoRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        private EnterpriseName enterpriseName;
        private EnterpriseType enterpriseType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Answer {
        private String sessionId;
        private String fieldId;
        private String type;
        private String answer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TailQuestion {
        private String sessionId;
        private String parentId;
    }

}
