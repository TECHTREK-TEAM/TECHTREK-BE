import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 20 },   // 초반 점진적 부하
        { duration: '30s', target: 50 },   // 최대 부하
        { duration: '30s', target: 0 },    // 종료
    ],
};

export default function () {
    const API_URL = 'http://localhost:8080/api/analyses/12';
    const params = {
        headers: {
            'Authorization': 'Bearer ',
            'Content-Type': 'application/json',
        },
    };

    let res = http.get(API_URL, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(Math.random() * 0.5 + 0.1);
}
