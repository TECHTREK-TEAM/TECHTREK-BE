import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 100,
    duration: '50s',
};

export default function () {
    // Redis 기반 tail QA에서는 sessionId만 있으면 이전 QA를 조회 가능
    let payload = JSON.stringify({
        sessionId: "b44cdd8c-f337-4e76-a553-d4a859ae6db2" // 테스트할 세션 ID
    });

    let res = http.post('http://localhost:8080/api/interview/questions/tail', payload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer <토큰>'
        }
    });

    check(res, {
        'tail question status is 200': (r) => r.status === 200,

        // question 이 루트 또는 data 아래 존재하면 통과
        'response has question': (r) => {
            const body = r.json();
            return body?.question !== undefined || body?.data?.question !== undefined;
        },
    });

    sleep(1);
}
