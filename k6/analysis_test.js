import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 50,
    duration: '10s',
};

export default function () {
    const API_URL = 'http://localhost:8080/api/analyses/3';
    const params = {
        headers: {
            'Authorization': 'Bearer <JWT>',
            'Content-Type': 'application/json',
        },
    };

    let res = http.get(API_URL, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
