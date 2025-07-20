
CREATE DATABASE IF NOT EXISTS omr_db;
USE omr_db;

CREATE TABLE IF NOT EXISTS omr_results (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  roll_no VARCHAR(50),
  answers TEXT,
  score INT
);

INSERT INTO omr_results (name, roll_no, answers, score) VALUES
('John Doe', '101', '{"Q1":"A","Q2":"B"}', 75),
('Jane Smith', '102', '{"Q1":"C","Q2":"D"}', 85);
