package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.session.entity.SessionInfo;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateSessionListDTO {

    // 세션 리스트 DTO
    public AnalysisResponse.SessionList exec(List<SessionInfo> sessionInfos) {
        List<AnalysisResponse.SessionData> sessionDataList = sessionInfos.stream()
                .map(session -> AnalysisResponse.SessionData.builder()
                        .sessionInfoId(String.valueOf(session.getId()))
                        .enterpriseName(session.getEnterpriseName())
                        .createdAt(session.getAnalysis().getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return AnalysisResponse.SessionList.builder()
                .session(sessionDataList)
                .build();
    }
}
