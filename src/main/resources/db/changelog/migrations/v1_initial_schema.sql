CREATE TABLE students
(
    id               INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_code     VARCHAR(20) UNIQUE NOT NULL,
    full_name        VARCHAR(100)       NOT NULL,
    dob              DATE,
    place_of_birth   VARCHAR(255),
    phone            VARCHAR(20),
    email            VARCHAR(100) UNIQUE,
    industry         VARCHAR(100),
    training_level   VARCHAR(50),
    type_of_training VARCHAR(50),
    current_position VARCHAR(100),
    avatar_url       TEXT,
    start_date       DATE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE courses
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(255)       NOT NULL,
    credits     INTEGER            NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_courses
(
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id   INTEGER REFERENCES students (id) ON DELETE CASCADE,
    course_id    INTEGER REFERENCES courses (id),
    semester     VARCHAR(10),
    grade        DECIMAL(4, 2),
    status       VARCHAR(20) CHECK (status IN ('COMPLETED', 'IN_PROGRESS', 'FAILED')),
    completed_at DATE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id INTEGER REFERENCES students (id) ON DELETE CASCADE,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    type       VARCHAR(50),
    is_read    BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_recommendations
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id  INTEGER REFERENCES students (id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    category    VARCHAR(50),
    action_url  TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE academic_goals
(
    student_id             INTEGER PRIMARY KEY REFERENCES students (id) ON DELETE CASCADE,
    total_credits_required INTEGER   DEFAULT 138,
    target_graduation_date DATE,
    note                   TEXT,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- -------------------------------------------------------
---------------- trigger:---------------------------------
CREATE
OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= now();
RETURN NEW;
END;
$$
language 'plpgsql';

CREATE TRIGGER update_student_modtime
    BEFORE UPDATE
    ON students
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_course_modtime
    BEFORE UPDATE
    ON courses
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_student_courses_modtime
    BEFORE UPDATE
    ON student_courses
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_notifications_modtime
    BEFORE UPDATE
    ON notifications
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_ai_recommendations_modtime
    BEFORE UPDATE
    ON ai_recommendations
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_academic_goals_modtime
    BEFORE UPDATE
    ON academic_goals
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();



-------------------------------
------seed data ---------------
INSERT INTO students (student_code, full_name, industry, training_level, current_position, email)
VALUES ('22120218', 'Lý Trường Nam', 'Information Technology', 'Bachelor', 'Front-end Developer', 'namt@gmail.com'),
       ('22120048', 'Nguyễn Chí Danh', 'Information Technology', 'Bachelor', 'Backend Developer', 'danh@gmail.com'),
       ('22120230', 'Phạm Tấn Nghĩa', 'Information Technology', 'Bachelor', 'Backend Developer', 'nghiap@gmail.com'),
       ('22120272', 'Hà Gia Phúc', 'Information Technology', 'Bachelor', 'AI Engineer', 'phuchg@gmail.com'),
       ('22120326', 'Nguyễn Trường Tân', 'Information Technology', 'Bachelor', 'Product Manager', 'tannt@gmail.com'),
       ('22120328', 'Trần Nhật Tân', 'Information Technology', 'Bachelor', 'Data Analyst', 'tannj@gmail.com');


INSERT INTO courses (course_code, course_name, credits, description)
VALUES ('IS201', 'Cơ sở dữ liệu', 4, 'Cung cấp kiến thức cơ bản về mô hình dữ liệu quan hệ và SQL.'),
       ('IS301', 'Phân tích thiết kế hệ thống', 4, 'Hướng dẫn quy trình phát triển phần mềm và các biểu đồ UML.'),
       ('IS402', 'Khai thác dữ liệu', 4, 'Các thuật toán phân lớp, gom cụm và luật kết hợp.'),
       ('IS210', 'Hệ thống thông tin doanh nghiệp', 3, 'Tìm hiểu về ERP, CRM và các quy trình nghiệp vụ.'),
       ('AI101', 'Nhập môn Trí tuệ nhân tạo', 4, 'Kiến thức nền tảng về tìm kiếm, suy luận và học máy.');


-- Giả sử Nam đã hoàn thành một số môn để đạt gần mốc 80 tín chỉ
INSERT INTO student_courses (student_id, course_id, semester, grade, status)
VALUES (1, 1, '2023.1', 9.0, 'COMPLETED'),
       (1, 2, '2023.2', 9.0, 'COMPLETED'),
       (1, 4, '2023.2', 9.0, 'COMPLETED'),
       (1, 5, '2024.1', 0.0, 'IN_PROGRESS');
-- Môn AI đang học

-- Gợi ý cá nhân hóa cho Nam (Front-end)
INSERT INTO ai_recommendations (student_id, title, description, category, action_url)
VALUES (1, 'Master React & Next.js', 'Lộ trình phát triển từ Front-end cơ bản lên chuyên sâu.', 'SKILL',
        '/roadmap/frontend'),
       (1, 'Học môn Thiết kế giao diện (UI/UX)', 'Môn học bổ trợ cực tốt cho vị trí hiện tại của bạn.', 'COURSE',
        '/courses/uiux-101');

-- Thông báo mẫu
INSERT INTO notifications (student_id, title, content, type)
VALUES (1, 'Lộ trình mới được cập nhật', 'AI đã cập nhật lộ trình học tập dựa trên điểm số học kỳ vừa rồi của bạn.',
        'SYSTEM'),
       (1, 'Tin nhắn từ Forum', 'Có 3 phản hồi mới trong bài đăng thảo luận về môn AI của bạn.', 'FORUM');

INSERT INTO academic_goals (student_id, total_credits_required, target_graduation_date)
VALUES (1, 138, '2026-06-30'),
       (2, 138, '2026-06-30');


