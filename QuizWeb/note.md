##DATABASE

---

-- =========================
-- Users
-- =========================
CREATE TABLE users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(100) NOT NULL UNIQUE,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL, -- store hashed password
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- Subjects
-- =========================
CREATE TABLE subjects (
    subject_id   INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(200) NOT NULL
);

-- =========================
-- Chapters
-- =========================
CREATE TABLE chapters (
    chapter_id   INT AUTO_INCREMENT PRIMARY KEY,
    subject_id   INT NOT NULL,
    chapter_name VARCHAR(200) NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

-- =========================
-- Quizzes
-- =========================
CREATE TABLE quizzes (
    quiz_id     INT AUTO_INCREMENT PRIMARY KEY,
    chapter_id  INT NOT NULL,
    quiz_title  VARCHAR(200) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chapter_id) REFERENCES chapters(chapter_id) ON DELETE CASCADE
);

-- =========================
-- Questions
-- =========================
CREATE TABLE questions (
    question_id    INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id        INT NOT NULL,
    question_text  TEXT NOT NULL,
    option_a       VARCHAR(255) NOT NULL,
    option_b       VARCHAR(255) NOT NULL,
    option_c       VARCHAR(255) NOT NULL,
    option_d       VARCHAR(255) NOT NULL,
    correct_option ENUM('A','B','C','D') NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- =========================
-- Results
-- =========================
CREATE TABLE results (
    result_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id     INT NOT NULL,
    quiz_id     INT NOT NULL,
    score       INT NOT NULL,
    taken_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);
