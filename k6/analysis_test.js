import http from 'k6/http';
import { check, sleep } from 'k6';

// 테스트 옵션 설정
export const options = {
    vus: 1,
    duration: "5s",
};

// 테스트 메인 함수
export default function () {
    // HTTP GET 요청을 보냄
    let res = http.get('http://localhost:8080/ping');

    console.log(`Status: ${res.status}`);
    console.log(`Body: ${res.body}`);

    // 응답 상태가 200인지 체크
    check(res, {
        'status was 200': (r) => r.status == 200,
        "body is pong": (r) => r.body === "pong"
    });

    // 1초 대기
    sleep(1);
}