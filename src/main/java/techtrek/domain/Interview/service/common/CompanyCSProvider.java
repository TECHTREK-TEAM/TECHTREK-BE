package techtrek.domain.Interview.service.common;

import org.springframework.stereotype.Component;

import java.util.Map;

// 회사별 CS 영역을 제공
@Component
public class CompanyCSProvider {
    private static final Map<String, String> COMPANY_CS = Map.of(
            "SAMSUNG", "운영체제, 네트워크, 자료구조, 알고리즘",
            "NAVER", "자료구조, 알고리즘, OS, 네트워크, 데이터베이스",
            "COUPANG", "자료구조, 알고리즘, 네트워크, 데이터베이스",
            "KAKAO", "알고리즘, 자료구조, 소프트웨어 설계, 데이터베이스",
            "BAEMIN", "데이터베이스, 네트워크, 사용자가 프론트/백엔드, 언어, 프레임워크를 무엇을 쓰는지 모르기 때문에 프레임워크 독립적이고 개념 중심으로 실무/설계 질문 작성",
            "DANGGEUN_MARKET", "데이터베이스, 운영체제, 자료구조, 소프트웨어 설계",
            "TOSS", "데이터베이스, 네트워크, 자료구조, 운영체제, 네트워크",
            "NEXON", "자료구조, 데이터베이스, 알고리즘"
    );

    public String exec(String companyName) {
        return COMPANY_CS.get(companyName);
    }
}
