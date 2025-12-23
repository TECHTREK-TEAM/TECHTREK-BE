import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    vus: 100, // 동시 사용자
    duration: '30s'
};

export default function () {
    const userId = `testUser_${__VU}`;
    http.post(`http://localhost:8080/api/test/login?userId=${userId}`);
    sleep(1);
}
