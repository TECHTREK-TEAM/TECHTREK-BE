-- 기업 테이블 생성
CREATE TABLE IF NOT EXISTS enterprise (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 기본질문 테이블 생성
CREATE TABLE IF NOT EXISTS basic_question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    enterprise_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    CONSTRAINT unique_question UNIQUE (question),
    CONSTRAINT fk_enterprise FOREIGN KEY (enterprise_id) REFERENCES enterprise(id)
);
