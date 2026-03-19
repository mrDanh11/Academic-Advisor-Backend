-- Table for Forum Categories (e.g., Academic Process)
CREATE TABLE forums
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    rules       TEXT,
    is_public   BOOLEAN   DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for Forum Posts
CREATE TABLE posts
(
    id             INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    forum_id       INTEGER REFERENCES forums (id) ON DELETE CASCADE,
    author_id     INTEGER REFERENCES students (id) ON DELETE CASCADE, -- Author
    title          VARCHAR(500) NOT NULL,
    content        TEXT         NOT NULL,
    tags           VARCHAR(255),                                       -- Stored as comma-separated or simple string
    views_count    INTEGER   DEFAULT 0,
    likes_count    INTEGER   DEFAULT 0,
    comments_count INTEGER   DEFAULT 0,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for Comments
CREATE TABLE comments
(
    id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    post_id    INTEGER REFERENCES posts (id) ON DELETE CASCADE,
    student_id INTEGER REFERENCES students (id) ON DELETE CASCADE, -- Commenter
    content    TEXT NOT NULL,
    parent_id  INTEGER REFERENCES comments (id) ON DELETE CASCADE, -- For nested replies
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table for Post Likes (Tracking who liked what)
CREATE TABLE post_likes
(
    post_id    INTEGER REFERENCES posts (id) ON DELETE CASCADE,
    student_id INTEGER REFERENCES students (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id, student_id)
);

-- -------------------------------------------------------
-- Triggers for updated_at
-- -------------------------------------------------------
CREATE TRIGGER update_forums_modtime
    BEFORE UPDATE ON forums
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_posts_modtime
    BEFORE UPDATE ON posts
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

CREATE TRIGGER update_comments_modtime
    BEFORE UPDATE ON comments
    FOR EACH ROW EXECUTE PROCEDURE update_modified_column();

-- -------------------------------------------------------
-- Seed Data for Testing (Real Topics)
-- -------------------------------------------------------
-- Forums
INSERT INTO forums (name, description, rules)
VALUES ('Định hướng chuyên ngành', 'Nơi trao đổi về các chuyên ngành Kỹ thuật phần mềm, Hệ thống thông tin, ATTT, Khoa học dữ liệu.', '1. Không bàn luận chính trị\n2. Tôn trọng ý kiến cá nhân\n3. Tránh hỏi các câu quá cơ bản đã có trong FAQ'),
       ('Góc học tập & Đồ án', 'Chia sẻ tài liệu, tìm nhóm làm đồ án, giải đáp thắc mắc về môn học.', '1. Ghi rõ mã môn học ở tiêu đề\n2. Không share link scam/virus'),
       ('Tuyển dụng & Internship', 'Thông tin thực tập và việc làm dành riêng cho sinh viên FIT-HCMUS.', '1. Chỉ post tin tuyển dụng liên quan IT\n2. Ghi rõ mức lương hoặc khoảng lương nếu có');

-- Posts (Author ID 1: Ly Truong Nam, ID 2: Nguyen Chi Danh)
-- Note: counts are set to match the actual number of records inserted below
INSERT INTO posts (forum_id, student_id, title, content, tags, views_count, likes_count, comments_count)
VALUES (1, 1, 'Nên chọn Chuyên ngành Kỹ thuật Phần mềm hay Khoa học Dữ liệu?', 'Chào mọi người, mình đang là sinh viên năm 2. Mình đang phân vân giữa SE và DS. SE thì mình thấy dễ xin việc hơn nhưng DS có vẻ là xu hướng tương lai. Anh chị đi trước cho mình xin lời khuyên với ạ!', 'ChuyênNgành, TưVấn, SE, DS', 150, 1, 1),
       (2, 2, 'Tài liệu ôn thi cuối kỳ môn Cấu trúc dữ liệu và Giải thuật', 'Mình có tổng hợp bộ slide và đề thi mẫu các năm của thầy Tùng. Bạn nào cần thì comment email mình gửi nhé hoặc check link drive bên dưới.', 'CTDL_GT, TàiLiệu, CS162', 450, 1, 1),
       (3, 1, 'Review phỏng vấn thực tập tại VNG (ZaloPay team)', 'Vừa rồi mình có tham gia phỏng vấn vị trí Backend Intern. Quy trình gồm 3 vòng: Test thuật toán, Technical interview và HR. Đề bài thuật toán chủ yếu về BFS/DFS và Hashmap...', 'Review, Internship, VNG, Backend', 890, 2, 2);

-- Comments (Conversations)
INSERT INTO comments (id, post_id, student_id, content) OVERRIDING SYSTEM VALUE
VALUES (1, 1, 2, 'Nếu bạn thích code thuần, build app thì SE. Còn nếu thích toán, thống kê và làm việc với dữ liệu thì chọn DS nhé. DS hiện tại ở VN yêu cầu khá cao về bằng cấp đấy.'),
       (2, 2, 1, 'Cảm ơn Danh nhiều nhé, đúng lúc mình đang cần tài liệu ôn phần đồ thị.'),
       (3, 3, 2, 'Chúc mừng Nam nhé! Cho mình hỏi vòng Technical họ có hỏi sâu về Database không bạn?');

-- Nested replies (Parent_id refers to the comments above)
INSERT INTO comments (post_id, student_id, content, parent_id)
VALUES (3, 1, 'Có nha Danh, họ hỏi kỹ về Index (B-Tree vs Hash) và các mức Isolation trong Transaction đó.', 3);

-- Likes
INSERT INTO post_likes (post_id, student_id)
VALUES (1, 2), -- Danh likes Nam's post 1
       (2, 1), -- Nam likes Danh's post 2
       (3, 2), -- Danh likes Nam's post 3
       (3, 1); -- Nam likes his own post 3 (to make it 2 likes)
