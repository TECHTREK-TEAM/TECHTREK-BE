package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.repository.TopCompany;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GetCompany {
    private final UserRepository userRepository;
    private final AnalysisRepository analysisRepository;

    // 관심기업 Top 3 조회
    public UserResponse.CompanyList exec(){
        // TODO:사용자 조회
        User user = userRepository.findById("1").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 상위 3개 기업 조회
        Pageable topThree = PageRequest.of(0, 3);
        List<TopCompany> topEnterprises = analysisRepository.findTopEnterprisesByUser(user, topThree);

        List<UserResponse.CompanyList.Company> companies = topEnterprises.stream()
                .map(p -> {
                    double avgScore = Math.round(p.getAvgScore() * 10) / 10.0;
                    return new UserResponse.CompanyList.Company(p.getName(), avgScore);
                })
                .toList();

        return UserResponse.CompanyList.builder()
                .companies(companies)
                .build();
    }
}
