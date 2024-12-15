INSERT INTO   role (description, name) VALUE ('ADMIN', 'ADMIN');
INSERT INTO   role (description, name) VALUE ('USER', 'USER');

INSERT INTO   categories (name, is_removed) VALUE ('Thức ăn cho chó', 0);
INSERT INTO   categories (name, is_removed) VALUE ('Thức ăn cho mèo', 0);

INSERT INTO doctor (name, specialization) VALUES ('Bác sĩ A', 'Nội khoa');
INSERT INTO doctor (name, specialization) VALUES ('Bác sĩ B', 'Ngoại khoa');

-- Thêm doctor trước
INSERT INTO doctor (id, name, specialization)
VALUES (1, 'Dr. John', 'General');

-- Sau đó mới thêm work_hour
INSERT INTO work_hour (doctor_id, start_time, end_time, is_available)
VALUES (1, '08:00', '12:00', true);

-- Sau đó chèn WorkHour với doctor_id hợp lệ
INSERT INTO work_hour (doctor_id, start_time, end_time) VALUES (1, '08:00', '12:00');
INSERT INTO work_hour (doctor_id, start_time, end_time) VALUES (2, '13:00', '17:00');