package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.sessionInfo.service.dao.GetEnterpriseNameCountDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCompanyBean {
    private final GetUserDAO getUserDAO;
    private final GetEnterpriseNameCountDAO getEnterpriseNameCountDAO;

    public UserResponse.CompanyList exec(){
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 기업이름에 따른 면접 개수 조회
        List<SessionInfoRepository.EnterpriseCount> enterpriseCount = getEnterpriseNameCountDAO.exec(user);

        // 총 면접 개수 계산
        long total = enterpriseCount.stream().mapToLong(SessionInfoRepository.EnterpriseCount::getCnt).sum();

        // 퍼센트 계산
        List<UserResponse.Company> companyList = new ArrayList<>();
        for (SessionInfoRepository.EnterpriseCount c : enterpriseCount) {
            double percent = total == 0 ? 0.0 : (c.getCnt() * 100.0 / total);
            percent = Math.round(percent * 10.0) / 10.0;
            companyList.add(new UserResponse.Company(c.getEnterpriseName(), percent));
        }

        // 내림차순
        companyList.sort((a, b) -> Double.compare(b.getCompanyPercent(), a.getCompanyPercent()));

        // 상위 3개
        List<UserResponse.Company> top3Companies = companyList.size() > 3 ? companyList.subList(0, 3) : companyList;

        return new UserResponse.CompanyList(top3Companies);

    }
}
