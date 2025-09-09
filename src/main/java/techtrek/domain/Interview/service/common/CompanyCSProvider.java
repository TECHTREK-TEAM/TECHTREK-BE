package techtrek.domain.Interview.service.common;

import org.springframework.stereotype.Component;

import java.util.Map;

// 회사별 CS 영역을 제공
@Component
public class CompanyCSProvider {
    private static final Map<String, String> COMPANY_CS = Map.of(
            "SAMSUNG", "운영체제, 네트워크, 자료구조, 알고리즘"
    );

    public String exec(String companyName) {
        return COMPANY_CS.get(companyName);
    }
}
