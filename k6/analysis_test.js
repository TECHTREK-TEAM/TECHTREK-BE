import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 200 },
        { duration: '30s', target: 400 },
        { duration: '30s', target: 0 },
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

    sleep(1);
}
