CREATE TABLE IF NOT EXISTS basic_question (
  id INT AUTO_INCREMENT PRIMARY KEY,
  question VARCHAR(255) NOT NULL,
  correct_answer VARCHAR(255) NOT NULL,
  enterprise_name VARCHAR(50) NOT NULL,
  category VARCHAR(50) NOT NULL,
  CONSTRAINT unique_question UNIQUE (question)
);