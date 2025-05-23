package techtrek.domain.sessionInfo.entity.status;


import java.util.Arrays;
import java.util.List;

public enum EnterpriseName {
    삼성전자("전통 CS 기본기", new String[]{
            "프로세스", "스레드", "캐시", "인덱스", "정규화", "정렬", "탐색", "tcp", "트랜잭션", "페이징", "스케줄링", "dfs", "bfs"
    }),
    쿠팡("대규모 트래픽, 인프라", new String[]{
            "cdn", "트래픽", "캐시전략", "샤딩", "장애복구", "메시지큐", "스케일링", "http"
    }),
    네이버("웹 아키텍처 이해", new String[]{
            "프록시", "http", "dns", "api최적화", "트랜잭션", "캐시전략", "msa", "프로세스"
    }),
    카카오("설명력 중심 + 실무적 질문", new String[]{
            "스레드", "프로세스", "흐름제어", "정규화", "맵", "인터럽트", "해시", "정렬"
    }),
    배달의민족("실무 기반 시스템 설계, 최적화, 데이터 처리", new String[]{
            "맵", "샤딩", "트랜잭션", "api최적화", "큐"
    }),
    당근마켓("최신 아키텍처 기반, 최적화, 데이터 처리", new String[]{
            "msa", "비동기", "메시지큐", "정규화"
    }),
    토스("Why 중심의 근본적 질문", new String[]{
            "tcp", "격리수준", "인덱스", "해시", "정규화"
    }),
    넥슨("게임 서버/동시성 특화", new String[]{
            "동기화", "스레드", "큐", "트래픽", "패킷", "스케줄링"
    });

    private final String description;
    private final String[] keywords;

    EnterpriseName(String description, String[] keywords) {
        this.description = description;
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getKeywords() {
        return Arrays.asList(keywords);
    }
}


