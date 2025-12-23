import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 500,
    duration: '50s',
};

export default function () {
    let payload = JSON.stringify({
        analysisId: 10
    });

    let res = http.post('http://localhost:8080/api/interview/questions/tail', payload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer <토큰>'
        }
    });

    check(res, {
        'tail question status is 200': (r) => r.status === 200,
        'response has question': (r) => r.json().data.question !== undefined,
    });

    sleep(1);
}
