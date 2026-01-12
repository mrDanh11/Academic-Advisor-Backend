UPDATE students 
SET 
    dob = '2004-10-23',
    place_of_birth = 'TP. Hồ Chí Minh',
    phone = '0901234567',
    type_of_training = 'Chính quy',
    avatar_url = 'https://ui-avatars.com/api/?name=Ly+Truong+Nam&background=random',
    start_date = '2022-09-05'
WHERE student_code = '22120218';

UPDATE students SET dob = '2004-01-01', place_of_birth = 'Đồng Nai', phone = '0908888777', type_of_training = 'Chính quy', avatar_url = 'https://ui-avatars.com/api/?name=Nguyen+Chi+Danh' WHERE student_code = '22120048';
UPDATE students SET dob = '2004-05-12', place_of_birth = 'Long An', phone = '0905555444', type_of_training = 'Chính quy', avatar_url = 'https://ui-avatars.com/api/?name=Pham+Tan+Nghia' WHERE student_code = '22120230';
UPDATE students SET dob = '2004-11-30', place_of_birth = 'Tiền Giang', phone = '0902222333', type_of_training = 'Chính quy', avatar_url = 'https://ui-avatars.com/api/?name=Ha+Gia+Phuc' WHERE student_code = '22120272';