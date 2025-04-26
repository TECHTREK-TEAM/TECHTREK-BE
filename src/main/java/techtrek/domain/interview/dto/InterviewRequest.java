package techtrek.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;

@Getter
@Setter
@AllArgsConstructor
public class InterviewRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        private String enterpriseName;
        private EnterpriseType enterpriseType;
    }
}
