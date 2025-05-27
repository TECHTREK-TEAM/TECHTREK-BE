package techtrek.domain.basicQuestion.entity.status;

import java.util.Optional;

public enum CSCategory {
    알고리즘(new String[]{"정렬", "탐색", "재귀", "백트래킹", "dp", "그리디", "이진탐색", "dfs", "bfs", "트리", "해시", "스택", "큐", "우선큐"}),
    자료구조(new String[]{"배열", "리스트", "스택", "큐", "힙", "맵", "그래프", "bst", "avl", "트라이"}),
    데이터베이스(new String[]{"인덱스", "트랜잭션", "정규화", "조인", "쿼리", "뷰", "프로시저", "격리수준", "mvcc", "교착상태", "nosql", "샤딩", "캐시전략", "메시지큐"}),
    네트워크(new String[]{"tcp", "http", "dns", "cdn", "프록시", "nat", "포트", "소켓", "qos", "arp", "흐름제어", "혼잡제어", "윈도우", "트래픽", "스케일링", "msa", "이벤트", "api최적화", "비동기", "패킷"}),
    컴퓨터구조(new String[]{"캐시", "파이프라인", "isa", "레지스터", "인터럽트", "사이클", "메모리", "cpu스케줄"}),
    운영체제(new String[]{"프로세스", "스레드", "컨텍스트", "스케줄링", "동기화", "세마포어", "가상메모리", "페이징", "스와핑", "시스템콜", "교착상태", "장애복구", "msa"});

    private final String[] keywords;

    CSCategory(String[] keywords) {
        this.keywords = keywords;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public static Optional<CSCategory> fromKeyword(String keyword) {
        for (CSCategory category : CSCategory.values()) {
            for (String k : category.getKeywords()) {
                if (k.equalsIgnoreCase(keyword)) {
                    return Optional.of(category);
                }
            }
        }
        return Optional.empty();
    }


}

