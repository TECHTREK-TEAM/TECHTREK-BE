package techtrek.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CsCategory;
import techtrek.domain.basicQuestion.repository.BasicQuestionRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BasicQuestionInitConfig {

    private final BasicQuestionRepository basicQuestionRepository;

    @PostConstruct
    public void init() {
        if (basicQuestionRepository.count() > 0) return;

        List<BasicQuestion> questions = List.of(
                new BasicQuestion("데이터베이스의 인덱스에 대해서 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("인덱스 테이블을 해시 테이블과 B+트리로 구성할때 각각이 어떤 차이점이 있을까요?", CsCategory.데이터베이스),
                new BasicQuestion("클러스터형 인덱스에 대해서 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("인덱스를 활용한 검색은 그렇지 않은 검색보다 무조건 빠를까요?", CsCategory.데이터베이스),
                new BasicQuestion("트랜잭션에 대해서 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("트랜잭션의 네가지 성질에 대해서 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("트랜잭션 독립성과 관련된 유명한 문제들을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("데이터베이스의 이상현상을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("RDB에서 정규형에 대해서 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("제 1정규형의 조건을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("제 2정규형의 조건을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("제 3정규형의 조건을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("보이스코드 정규형의 조건을 설명해주세요.", CsCategory.데이터베이스),
                new BasicQuestion("stable, unstable에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("In-place sort에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("부분적으로 정렬되어 있는 상태에서 사용하면 좋은 알고리즘은?", CsCategory.알고리즘),
                new BasicQuestion("버블정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("선택정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("삽입정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("셸정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("힙정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("합병정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("퀵정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("기수정렬에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("BFS에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("백트레킹에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("이진탐색에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("Parametric Search에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("Union-Find에 대해서 설명해주세요.", CsCategory.알고리즘),
                new BasicQuestion("배열과 연결리스트의 차이에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("연결리스트의 종류에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("연결리스트에서 중간 요소을 어떻게 효율적으로 접근할 수 있는가?", CsCategory.자료구조),
                new BasicQuestion("스택에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("큐에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("선형큐와 원형큐에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("그래프에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("트리와 그래프의 차이점을 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("최소 신장 트리(MST)에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("트리에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("배열과 트리의 차이점에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("이진트리에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("이진탐색트리에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("힙에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("이진힙과 BST의 차이점에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("해시테이블에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("해시테이블에서 데이터 액세스할 때의 시간 복잡도에 대해서 설명해주세요.", CsCategory.자료구조),
                new BasicQuestion("map과 set의 차이점은?", CsCategory.자료구조),
                new BasicQuestion("부동소수점 연산에서 교환법칙이 성립할까요?", CsCategory.자료구조),
                new BasicQuestion("네트워크란?", CsCategory.네트워크),
                new BasicQuestion("OSI 7계층이란 무엇이고 왜 계층으로 나눈 것인가?", CsCategory.네트워크),
                new BasicQuestion("OSI 7계층의 각 특징을 알려주세요", CsCategory.네트워크),
                new BasicQuestion("전송계층의 역할", CsCategory.네트워크),
                new BasicQuestion("TCP란?", CsCategory.네트워크),
                new BasicQuestion("3way handshake이란?", CsCategory.네트워크),
                new BasicQuestion("4way handshake이란?", CsCategory.네트워크),
                new BasicQuestion("3way handshake와 4way handshake가 차이나는 이유는?", CsCategory.네트워크),
                new BasicQuestion("TCP의 흐름제어, 혼잡제어란?", CsCategory.네트워크),
                new BasicQuestion("TCP의 헤더?", CsCategory.네트워크),
                new BasicQuestion("UDP란?", CsCategory.네트워크),
                new BasicQuestion("패킷교환 방식에 대해 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("IP주소는 어떻게 구성되어 있는가?", CsCategory.네트워크),
                new BasicQuestion("IP(IPv4)주소는 고갈되었다. 이를 해결하기 위한 방법이 있을까?", CsCategory.네트워크),
                new BasicQuestion("공인IP와 사설IP에 대해서 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("NAT기술에 대해 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("고정 IP와 유동 IP에 대해서 설명해 주세요", CsCategory.네트워크),
                new BasicQuestion("http와 https에 대해서 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("http 1.1과 비교해 2.0의 특징에 대해서 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("http 응답코드 종류에 설명해주세요", CsCategory.네트워크),
                new BasicQuestion("http 메서드 종류에는 뭐가 있을까요", CsCategory.네트워크),
                new BasicQuestion("DHCP란?", CsCategory.네트워크),
                new BasicQuestion("DNS란?", CsCategory.네트워크),
                new BasicQuestion("프로세스와 스레드에 대해 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("프로세스의 구성에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("멀티 프로세스와 멀티 스레드에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("운영체제의 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("장기 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("중기 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("단기 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("FCFS 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("Round Robin 스케줄링에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("경쟁 상태에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("뮤텍스와 세마포어에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("교착 상태에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("교착 상태의 발생조건에 대해서 자세히 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("교착 상태의 해결방법에 대해서 설명해주세요.", CsCategory.운영체제),
                new BasicQuestion("캐시 메모리란 무엇이며, 캐시의 지역성(Locality)이란 어떤 개념인가요?", CsCategory.컴퓨터구조)
        );

        basicQuestionRepository.saveAll(questions);
    }
}
