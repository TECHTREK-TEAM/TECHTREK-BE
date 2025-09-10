package techtrek.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;
import techtrek.domain.interviewQuestion.repository.InterviewQuestionRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    ApplicationRunner initializeDatabase(EnterpriseRepository enterpriseRepository,
                                         InterviewQuestionRepository questionRepository) {
        return args -> {
            // ---------------------------
            // 1️⃣ Enterprise 데이터 삽입
            // ---------------------------
            List<String> enterpriseNames = List.of(
                    "SAMSUNG", "COUPANG", "NAVER", "KAKAO",
                    "BAEMIN", "DANGGEUN_MARKET", "TOSS", "NEXON"
            );

            for (String name : enterpriseNames) {
                enterpriseRepository.findByName(name)
                        .orElseGet(() -> enterpriseRepository.save(
                                Enterprise.builder().name(name).build()
                        ));
            }

            Enterprise samsung = enterpriseRepository.findByName("SAMSUNG").orElseThrow();

            // ---------------------------
            // 2️⃣ InterviewQuestion 데이터 삽입
            // ---------------------------
            List<InterviewQuestion> questions = List.of(
                    new InterviewQuestion("멀티 스레드와 멀티 프로세스 차이는 무엇인가요?",
                            "스레드는 같은 메모리 공간, 경량 / 프로세스: 독립된 메모리 공간, 무거움",
                            samsung, "OS"),
                    new InterviewQuestion("Thread와 Process의 차이는 무엇인가요?",
                            "Thread: 실행 단위 / Process: 자원 단위, 독립 실행",
                            samsung, "OS"),
                    new InterviewQuestion("동기와 비동기란 무엇인가요?",
                            "동기: 순차 실행 / 비동기: 비순차 실행, 기다리지 않고 처리",
                            samsung, "OS"),
                    new InterviewQuestion("세마포어 개념을 설명해 주세요.",
                            "공유 자원 접근 제어, 신호 기반 동기화",
                            samsung, "OS"),
                    new InterviewQuestion("크리티컬 섹션이란 무엇인가요?",
                            "한 번에 하나의 스레드만 접근 가능한 코드 영역",
                            samsung, "OS"),
                    new InterviewQuestion("데드락(Deadlock) 설명해 주세요.",
                            "서로 자원을 점유하며 무한 대기 상태",
                            samsung, "OS"),
                    new InterviewQuestion("OS에 대해 설명해 주세요.",
                            "하드웨어와 소프트웨어 간 인터페이스 제공, 자원 관리",
                            samsung, "OS"),
                    new InterviewQuestion("힙과 스택 설명해 주세요.",
                            "스택: LIFO, 지역 변수 / 힙: 동적 메모리 할당",
                            samsung, "OS"),
                    new InterviewQuestion("배열(Array)과 리스트(List)의 차이와 검색에 더 용이한 것은 어떤거고 이유는 무엇인가요?",
                            "배열은 고정 크기 연속 메모리로 인덱스 접근이 빨라 검색에 유리, 리스트는 동적 크기와 삽입/삭제 유리.",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("배열(Array)과 연결 리스트(Linked List)의 차이점과 장단점은 무엇인가요?",
                            "배열: 연속 메모리, 빠른 인덱스 접근 / 연결 리스트: 포인터 기반, 삽입/삭제 유리",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("Array, List, 배열의 차이점은 무엇인가요?",
                            "Array: 고정 크기 / List: 동적 / 배열: 일반 용어",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("해시 테이블(Hash Table)이란 무엇인가요?",
                            "키-값 쌍 저장, 빠른 검색",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("해시 테이블 충돌을 해결하는 방법은 무엇인가요?",
                            "체이닝이나 개방 주소법으로 해결.",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("이진트리와 이진 탐색 트리 차이점은 무엇인가요?",
                            "이진트리: 일반 트리 / 이진 탐색 트리: 정렬 유지",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("그래프와 인접 리스트/인접 행렬 표현 방법의 차이는 무엇인가요?",
                            "리스트: 메모리 효율적 / 행렬: 빠른 연결 확인",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("스택(Stack)과 큐(Queue)의 동작 방식은 무엇인가요?",
                            "스택: LIFO / 큐: FIFO",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("힙과 스택에 대해 설명해주세요.",
                            "힙: 동적 메모리 / 스택: 함수 호출, 지역 변수 저장",
                            samsung, "DATA_STRUCTURE"),
                    new InterviewQuestion("퀵소트(Quick Sort) 알고리즘이 무엇인가요?",
                            "피벗 기준으로 분할해 재귀적으로 정렬하는 효율적 정렬 알고리즘.",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("버블 정렬, 선택 정렬, 삽입 정렬의 차이점은 무엇인가요?",
                            "버블: 인접 비교 / 선택: 최소값 선택 / 삽입: 정렬된 위치 삽입",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("DFS와 BFS의 차이는 무엇인가요?",
                            "DFS: 깊이 우선 / BFS: 너비 우선",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("BFS 알고리즘에 대해 설명해주세요.",
                            "그래프/트리 너비 우선 탐색",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("DFS 알고리즘에 대해 설명해주세요.",
                            "그래프/트리 깊이 우선 탐색",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("이진 탐색(Binary Search) 동작 원리는 무엇인가요?",
                            "정렬된 배열에서 중간값과 비교 후 탐색 범위 반으로 축소",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("재귀(Recursion)와 반복(Iteration)의 차이는 무엇인가요?",
                            "재귀: 자기 자신 호출 / 반복: 루프 사용",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("다이나믹 프로그래밍(Dynamic Programming) 개념은 무엇인가요?",
                            "문제를 작은 문제로 나누고 결과 재사용",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("탐색 알고리즘에는 어떤 것들이 있나요?",
                            "선형, 이진, 해시 탐색 등",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("재귀함수가 무엇인가요? 재귀함수 장단점은?",
                            "자기 자신을 호출 / 장점: 코드 간결, 단점: 스택 오버플로우 가능",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("XML 파싱하는 알고리즘을 간략히 설명해 주세요.",
                            "DOM: 문서 전체 로드, SAX: 이벤트 기반 순차 처리",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("탐색 알고리즘에 대해 설명해 주세요.",
                            "선형, 이진, 그래프 탐색 등 데이터 탐색 방법",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("버블 정렬, 선택 정렬, 삽입 정렬의 차이점은 무엇인가요?",
                            "버블: 인접 교환 / 선택: 최소값 선택 / 삽입: 정렬 위치 삽입",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("DFS와 BFS 차이점을 설명해주세요.",
                            "DFS는 깊이 우선, BFS는 너비 우선으로 탐색하며 BFS는 최단 경로에 유리.",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("해시 알고리즘에 대해 설명해주세요.",
                            "데이터를 고정 크기 값으로 변환해 검색, 인증, 무결성에 활용.",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("선형 알고리즘에 대해 설명해주세요.",
                            "연산이 입력 크기에 비례하는 알고리즘",
                            samsung, "ALGORITHM"),
                    new InterviewQuestion("OSI 7 Layer 설명해 주세요.",
                            "물리-데이터링크-네트워크-전송-세션-표현-응용 계층으로 구성.",
                            samsung, "NETWORK"),
                    new InterviewQuestion("TCP/IP 설명해 주세요.",
                            "전송 계층(TCP/UDP) + 인터넷 계층(IP) 기반 통신 프로토콜",
                            samsung, "NETWORK"),
                    new InterviewQuestion("TCP/UDP 차이 설명해 주세요.",
                            "TCP: 연결 지향, 신뢰성 보장 / UDP: 비연결, 속도 우선",
                            samsung, "NETWORK"),
                    new InterviewQuestion("IoT 실시간 통신에 사용할 수 있는 프로토콜 두 가지와 특징은 무엇인가요?",
                            "MQTT: 경량, 저전력 / CoAP: REST 기반, 제약 환경 적합",
                            samsung, "NETWORK"),
                    new InterviewQuestion("프로토콜에 대해 설명헤주세요.",
                            "통신 규칙과 데이터 교환 방식 정의.",
                            samsung, "NETWORK"),
                    new InterviewQuestion("웹소켓 통신과 HTTP 요청/응답 방식의 차이는 무엇인가요?",
                            "웹소켓: 양방향 실시간 / HTTP: 요청-응답 단방향",
                            samsung, "NETWORK"),
                    new InterviewQuestion("DB의 통신 방법은 무엇인가요?",
                            "SQL 쿼리, JDBC/ODBC 등 클라이언트-서버 통신",
                            samsung, "DATABASE"),
                    new InterviewQuestion("Connection Pool이란 무엇인가요?",
                            "DB 연결 재사용으로 성능 향상",
                            samsung, "DATABASE"),
                    new InterviewQuestion("DBA 스키마 사상 논하세요.",
                            "논리-물리 구조 매핑 과정",
                            samsung, "DATABASE"),
                    new InterviewQuestion("RDBS에 대해서 이야기해 주세요.",
                            "관계형 DB 관리 시스템, 테이블 기반 저장",
                            samsung, "DATABASE"),
                    new InterviewQuestion("SQL과 NoSQL의 다른 점은 무엇인가요?",
                            "SQL: 정형 데이터, 스키마 필요 / NoSQL: 비정형, 유연",
                            samsung, "DATABASE"),
                    new InterviewQuestion("Hash 함수에 대해 설명해주세요.",
                            "입력 데이터를 고정 길이 값으로 매핑, 검색/보안 활용",
                            samsung, "DATABASE"),
                    new InterviewQuestion("디자인패턴에 대해 설명해 주세요.",
                            "반복되는 문제 해결을 위해 설계된 재사용 가능한 템플릿",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("Singleton이란 무엇인가요?",
                            "인스턴스를 하나만 생성하고 전역에서 접근 가능하도록 하는 패턴",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("Singleton Pattern 설명해 주세요.",
                            "클래스 인스턴스를 단 하나만 생성, 전역 접근 제공",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("Object Oriented Programming은 무엇인가요?",
                            "객체와 클래스 기반 프로그래밍 패러다임",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("객체지향과 절차지향 언어의 차이점은 무엇인가요?",
                            "객체지향: 데이터+메서드 / 절차지향: 순차적 코드",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("추상클래스와 인터페이스의 차이점은 무엇인가요?",
                            "추상클래스: 일부 구현 가능 / 인터페이스: 구현 불가, 다중 상속 가능",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("Override와 Overload의 차이는 무엇인가요?",
                            "Override: 상속 메서드 재정의 / Overload: 같은 이름 다른 매개변수",
                            samsung, "SOFTWARE_DESIGN"),
                    new InterviewQuestion("다형성에 대해 설명해 주세요.",
                            "동일 인터페이스로 다양한 객체를 다루는 능력",
                            samsung, "SOFTWARE_DESIGN")
            );

            // ---------------------------
            // 3️⃣ 중복 체크 후 저장
            // ---------------------------
            for (InterviewQuestion q : questions) {
                if (!questionRepository.existsByQuestion(q.getQuestion())) {
                    questionRepository.save(q);
                }
            }
        };
    }
}
