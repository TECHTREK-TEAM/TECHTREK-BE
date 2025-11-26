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
    const API_URL = 'http://localhost:8080/api/analyses/5';
    const params = {
        headers: {
            'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXZlcl9BQUFBT2E2NzRoZEl4RUtRcFEtR0lLOEFtTUFVR0Q3bEtUQUQwUVpPaC1KV3ctYUJSSU9KZ3FxYUlSTHExcTZSZFhHZXBzTFpRU0lucDY3SHh4V2VGdXBIb2RBIiwiaWF0IjoxNzY0MTM0MjM0LCJleHAiOjE3NjQyMjA2MzR9.9Jow92GBN0pGIXApQHTRyVa4E0FMVLKmxpZExlrvd2s',
            'Content-Type': 'application/json',
        },
    };

    let res = http.get(API_URL, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.1);
}
