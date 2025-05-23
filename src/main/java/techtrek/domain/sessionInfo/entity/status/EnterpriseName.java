package techtrek.domain.sessionInfo.entity.status;


public enum EnterpriseName {
    삼성전자("전통 CS 기본기"),
    쿠팡("대규모 트래픽, 인프라"),
    네이버("웹 아키텍처 이해"),
    카카오("설명력 중심 + 실무적 질문"),
    배달의민족("실무 기반 시스템 설계, 최적화, 데이터 처리"),
    당근마켓("최신 아키텍처 기반, 최적화, 데이터 처리"),
    토스("Why 중심의 근본적 질문"),
    넥슨( "게임 서버/동시성 특화");

    private final String description;

    EnterpriseName(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

