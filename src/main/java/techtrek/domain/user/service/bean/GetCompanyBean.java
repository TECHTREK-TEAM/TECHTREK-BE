package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoListDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.CreateCompanyDTO;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetCompanyBean {

    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final CreateCompanyDTO createCompanyDTO;

    // 관심기업 Top 3 조회
    public UserResponse.CompanyList exec(CustomUserDetails userDetails){
        // 사용자, 세션정보 조회
        User user = getUserDAO.exec(userDetails.getId());
        List<SessionInfo> sessionInfos = getSessionInfoListDAO.exec(user);

        // 기업이름에 따른 면접 개수 조회
        Map<EnterpriseName, Long> countMap = new HashMap<>();
        for (SessionInfo sessionInfo : sessionInfos) {
            EnterpriseName name = sessionInfo.getEnterpriseName();
            countMap.put(name, countMap.getOrDefault(name, 0L) + 1);
        }

        // 전체 면접 수
        long total = sessionInfos.size();

        // 비율 계산 및 DTO 생성
        List<UserResponse.CompanyList.Company> top3Companies = countMap.entrySet().stream()
                .map(entry -> {
                    double percent = (entry.getValue() * 100.0) / total;
                    percent = Math.round(percent * 10.0) / 10.0; // 소수점 첫째자리 반올림
                    return new UserResponse.CompanyList.Company(entry.getKey(), percent);
                })
                .sorted(Comparator.comparingDouble(UserResponse.CompanyList.Company::getCompanyPercent).reversed()) // 내림차순
                .limit(3) // 상위 3개
                .collect(Collectors.toList());

        return createCompanyDTO.exec(top3Companies);

    }
}
