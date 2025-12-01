import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 100,
    duration: '50s',
};

export default function () {
    // Redis 기반 tail QA에서는 sessionId만 있으면 이전 QA를 조회 가능
    let payload = JSON.stringify({
        sessionId: "b979913f-3f4d-4720-a2b8-0e4bf75299fb" // 테스트할 세션 ID
    });

    let res = http.post('http://localhost:8080/api/interview/questions/tail', payload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer <토큰>'
        }
    });

    check(res, {
        'tail question status is 200': (r) => r.status === 200,
        // 안전하게 question 존재 여부 확인
        'response has question': (r) => r.status === 200 && r.json().question !== undefined,
    });

    sleep(1);
}
