package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RedisResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhaseCount {
        private String phase;
        private int count;
    }
}
