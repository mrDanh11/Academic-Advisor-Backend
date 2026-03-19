-- v4_forum_enhanced.sql
-- ADD AUTHOR AND TYPE TO FORUMS
ALTER TABLE forums ADD COLUMN author_id INTEGER REFERENCES students(id);
ALTER TABLE forums ADD COLUMN type VARCHAR(20) DEFAULT 'GLOBAL'; -- GLOBAL (Public), PRIVATE (Classroom/Group)
ALTER TABLE forums ADD COLUMN members_count INTEGER DEFAULT 0;
ALTER TABLE forums ADD COLUMN avatar_url VARCHAR(255);

-- MEMBERSHIP TABLE FOR PRIVATE FORUMS
CREATE TABLE forum_memberships (
    id SERIAL PRIMARY KEY,
    forum_id INTEGER NOT NULL REFERENCES forums(id),
    student_id INTEGER NOT NULL REFERENCES students(id),
    role VARCHAR(20) DEFAULT 'MEMBER', -- OWNER, ADMIN, MEMBER
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(forum_id, student_id)
);

-- UPDATE FORUMS DATA (Real-world scenarios)
-- Convert existing forums to GLOBAL and assign an admin as author (Student ID 1)
UPDATE forums SET 
    author_id = 1, 
    type = 'GLOBAL', 
    avatar_url = 'https://api.dicebear.com/7.x/initials/svg?seed=Global',
    members_count = (SELECT COUNT(*) FROM students),
    created_at = CURRENT_TIMESTAMP
WHERE type IS NULL;

-- INSERT NEW PRIVATE FORUMS (Classroom/Group)
INSERT INTO forums (name, description, rules, is_public, type, author_id, avatar_url, members_count, created_at)
VALUES 
('Lớp CS-101: Nhập môn lập trình', 'Nơi thảo luận kín của lớp CS-101. Chỉ thành viên lớp mới thấy bài viết.', 'Không spam, tốn trọng giảng viên.', FALSE, 'PRIVATE', 2, 'https://api.dicebear.com/7.x/shapes/svg?seed=CS101', 3, CURRENT_TIMESTAMP - INTERVAL '10 days'),
('Nhóm đồ án: Academic Advisor', 'Dành riêng cho nhóm thực hiện DATN.', 'Báo cáo tiến độ mỗi ngày.', FALSE, 'PRIVATE', 1, 'https://api.dicebear.com/7.x/shapes/svg?seed=DATN', 2, CURRENT_TIMESTAMP - INTERVAL '5 days'),
('CLB Cờ Vua FIT', 'Nơi sinh hoạt nội bộ của CLB.', 'Vui vẻ hòa đồng.', FALSE, 'PRIVATE', 3, 'https://api.dicebear.com/7.x/shapes/svg?seed=Chess', 1, CURRENT_TIMESTAMP - INTERVAL '1 day');

-- INSERT MEMBERSHIPS FOR PRIVATE FORUMS
-- CS-101: Student ID 2, 1, 3 (Student 2 is author)
INSERT INTO forum_memberships (forum_id, student_id, role) VALUES 
((SELECT id FROM forums WHERE name = 'Lớp CS-101: Nhập môn lập trình'), 2, 'OWNER'),
((SELECT id FROM forums WHERE name = 'Lớp CS-101: Nhập môn lập trình'), 1, 'MEMBER'),
((SELECT id FROM forums WHERE name = 'Lớp CS-101: Nhập môn lập trình'), 3, 'MEMBER');

-- DATN: Student ID 1, 2
INSERT INTO forum_memberships (forum_id, student_id, role) VALUES 
((SELECT id FROM forums WHERE name = 'Nhóm đồ án: Academic Advisor'), 1, 'OWNER'),
((SELECT id FROM forums WHERE name = 'Nhóm đồ án: Academic Advisor'), 2, 'MEMBER');

-- Chess: Student ID 3
INSERT INTO forum_memberships (forum_id, student_id, role) VALUES 
((SELECT id FROM forums WHERE name = 'CLB Cờ Vua FIT'), 3, 'OWNER');

-- ADD PRIVATE POSTS FOR THESE FORUMS (To test visibility)
INSERT INTO posts (forum_id, student_id, title, content, views_count, likes_count, comments_count, tags)
VALUES 
((SELECT id FROM forums WHERE name = 'Lớp CS-101: Nhập môn lập trình'), 2, 'Thông báo bài tập về nhà tuần 4', 'Các bạn nhớ làm bài tập con trỏ nhé.', 10, 2, 1, ARRAY['homework', 'c-plus-plus']),
((SELECT id FROM forums WHERE name = 'Nhóm đồ án: Academic Advisor'), 1, 'Cập nhật UI thiết kế v4', 'Tôi vừa thêm chức năng Private Forum vào database.', 5, 1, 0, ARRAY['dev', 'database']),
((SELECT id FROM forums WHERE name = 'CLB Cờ Vua FIT'), 3, 'Lịch thi đấu giải mở rộng', 'Chúng ta sẽ thi đấu vào chủ nhật này.', 12, 4, 2, ARRAY['tournament', 'chess']);

-- SYNC ALL Global Forums for all students (Self-membership for global is optional but often implicitly assumed)
-- For this logic, we will assume GLOBAL = accessible to all, PRIVATE = accessible only if record exists in forum_memberships.
