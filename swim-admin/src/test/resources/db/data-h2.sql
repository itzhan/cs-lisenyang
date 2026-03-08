INSERT INTO sys_role (id, role_key, role_name, status, deleted) VALUES
  (1, 'ADMIN', '管理员', 1, 0),
  (2, 'USER', '普通用户', 1, 0),
  (3, 'COACH', '教练', 1, 0);

INSERT INTO sys_user (id, username, password, real_name, phone, email, status, deleted) VALUES
  (1, 'admin', '{noop}admin123', '系统管理员', '13800000001', 'admin@xiangyongshe.com', 1, 0),
  (2, 'user01', '{noop}user123', '张同学', '13800000002', 'user01@xiangyongshe.com', 1, 0),
  (3, 'coach01', '{noop}coach123', '李教练', '13800000003', 'coach01@xiangyongshe.com', 1, 0);
INSERT INTO sys_user (id, username, password, real_name, phone, email, status, deleted) VALUES
  (4, 'user02', '{noop}user123', '李同学', '13800000004', 'user02@xiangyongshe.com', 1, 0),
  (5, 'user03', '{noop}user123', '王同学', '13800000005', 'user03@xiangyongshe.com', 1, 0),
  (6, 'coach02', '{noop}coach123', '王教练', '13800000006', 'coach02@xiangyongshe.com', 1, 0),
  (7, 'coach03', '{noop}coach123', '赵教练', '13800000007', 'coach03@xiangyongshe.com', 1, 0),
  (8, 'admin02', '{noop}admin123', '运营管理员', '13800000008', 'admin02@xiangyongshe.com', 1, 0);

INSERT INTO sys_user_role (user_id, role_id) VALUES
  (1, 1),
  (2, 2),
  (3, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES
  (4, 2),
  (5, 2),
  (6, 3),
  (7, 3),
  (8, 1);

INSERT INTO coach_profile (id, user_id, qualification, specialty, intro, avatar_url, status, deleted) VALUES
  (1, 3, '国家游泳教练证书', '自由泳/蛙泳', '10年教学经验，擅长初学者与提升训练', '', 1, 0);
INSERT INTO coach_profile (id, user_id, qualification, specialty, intro, avatar_url, status, deleted) VALUES
  (2, 6, '省级教练证书', '蝶泳/仰泳', '7年教学经验，擅长动作纠错与体能训练', '', 1, 0),
  (3, 7, '社会体育指导员', '少儿启蒙', '擅长少儿兴趣引导与水中安全教育', '', 1, 0);

INSERT INTO course (id, course_name, course_type, level, duration_min, price, capacity, cover_url, description, status, deleted) VALUES
  (1, '基础游泳入门课', '团课', '初级', 60, 79.00, 20, '', '适合零基础学员，掌握基础动作与呼吸技巧。', 1, 0),
  (2, '自由泳提升课', '小班', '中级', 90, 129.00, 12, '', '针对自由泳技术提升，纠正动作与提升效率。', 1, 0);
INSERT INTO course (id, course_name, course_type, level, duration_min, price, capacity, cover_url, description, status, deleted) VALUES
  (3, '少儿启蒙班', '小班', '初级', 60, 99.00, 15, '', '适合6-12岁儿童水中启蒙与安全教育。', 1, 0),
  (4, '蛙泳专项课', '私教', '中级', 60, 199.00, 1, '', '针对蛙泳动作专项纠错。', 1, 0),
  (5, '体能+水感训练', '团课', '初级', 45, 59.00, 25, '', '提升体能与水中适应能力。', 1, 0),
  (6, '竞技提升班', '小班', '高级', 120, 199.00, 8, '', '针对竞技选手的技术提升。', 1, 0);

INSERT INTO course_schedule (id, course_id, coach_id, start_time, end_time, course_date, location, max_capacity, current_count, status, deleted) VALUES
  (1, 1, 1, '2026-02-05 19:00:00', '2026-02-05 20:00:00', '2026-02-05', '一号泳池', 20, 1, 1, 0),
  (2, 2, 1, '2026-02-06 19:00:00', '2026-02-06 20:30:00', '2026-02-06', '二号泳池', 12, 1, 1, 0),
  (3, 3, 2, '2026-02-07 09:00:00', '2026-02-07 10:00:00', '2026-02-07', '三号泳池', 15, 2, 1, 0),
  (4, 4, 2, '2026-02-07 14:00:00', '2026-02-07 15:00:00', '2026-02-07', '私教区', 1, 1, 1, 0),
  (5, 5, 3, '2026-02-08 18:30:00', '2026-02-08 19:15:00', '2026-02-08', '一号泳池', 25, 0, 1, 0),
  (6, 6, 2, '2026-02-09 20:00:00', '2026-02-09 22:00:00', '2026-02-09', '二号泳池', 8, 1, 1, 0),
  (7, 1, 1, '2026-02-10 19:00:00', '2026-02-10 20:00:00', '2026-02-10', '一号泳池', 20, 0, 1, 0),
  (8, 2, 1, '2026-02-11 19:00:00', '2026-02-11 20:30:00', '2026-02-11', '二号泳池', 12, 1, 1, 0),
  (9, 3, 3, '2026-02-12 10:00:00', '2026-02-12 11:00:00', '2026-02-12', '三号泳池', 15, 1, 1, 0),
  (10, 5, 2, '2026-02-13 08:00:00', '2026-02-13 08:45:00', '2026-02-13', '一号泳池', 25, 0, 0, 0);

INSERT INTO course_schedule_coach (schedule_id, coach_id, deleted) VALUES
  (1, 1, 0),
  (2, 1, 0);
INSERT INTO course_schedule_coach (schedule_id, coach_id, deleted) VALUES
  (3, 2, 0),
  (3, 3, 0),
  (4, 2, 0),
  (5, 3, 0),
  (6, 2, 0),
  (6, 3, 0),
  (8, 1, 0),
  (8, 2, 0),
  (9, 3, 0),
  (10, 2, 0);

INSERT INTO reservation (id, schedule_id, user_id, reserve_time, status, deleted, create_time) VALUES
  (1, 1, 2, '2026-02-01 10:00:00', 1, 0, '2026-01-19 10:05:00');
INSERT INTO reservation (id, schedule_id, user_id, reserve_time, status, cancel_time, cancel_reason, deleted, create_time) VALUES
  (2, 3, 4, '2026-02-01 11:00:00', 1, NULL, NULL, 0, '2026-01-20 11:00:00'),
  (3, 3, 5, '2026-02-01 11:05:00', 1, NULL, NULL, 0, '2026-01-21 11:05:00'),
  (4, 4, 4, '2026-02-02 09:00:00', 3, NULL, NULL, 0, '2026-01-22 09:00:00'),
  (5, 6, 5, '2026-02-03 15:00:00', 1, NULL, NULL, 0, '2026-01-23 15:00:00'),
  (6, 8, 2, '2026-02-04 16:00:00', 2, '2026-02-04 18:00:00', '临时有事', 0, '2026-01-24 16:00:00'),
  (7, 9, 4, '2026-02-04 17:00:00', 4, NULL, NULL, 0, '2026-01-24 17:00:00'),
  (8, 2, 5, '2026-02-02 12:00:00', 1, NULL, NULL, 0, '2026-01-25 12:00:00');

INSERT INTO orders (id, order_no, user_id, reservation_id, amount, pay_status, pay_type, pay_time, refund_status, deleted, create_time) VALUES
  (1, 'ORD202602010001', 2, 1, 79.00, 1, 3, '2026-02-01 10:02:00', 0, 0, '2026-01-19 10:10:00');
INSERT INTO orders (id, order_no, user_id, reservation_id, amount, pay_status, pay_type, pay_time, refund_status, refund_time, deleted, create_time) VALUES
  (2, 'ORD202602010002', 4, 2, 99.00, 1, 3, '2026-02-01 11:02:00', 0, NULL, 0, '2026-01-20 11:05:00'),
  (3, 'ORD202602010003', 5, 3, 99.00, 0, 3, NULL, 0, NULL, 0, '2026-01-21 11:06:00'),
  (4, 'ORD202602020001', 4, 4, 199.00, 1, 1, '2026-02-02 09:05:00', 0, NULL, 0, '2026-01-22 09:06:00'),
  (5, 'ORD202602030001', 5, 5, 199.00, 1, 2, '2026-02-03 15:05:00', 0, NULL, 0, '2026-01-23 15:06:00'),
  (6, 'ORD202602040001', 2, 6, 129.00, 2, 2, '2026-02-04 16:10:00', 1, '2026-02-04 18:10:00', 0, '2026-01-24 16:12:00'),
  (7, 'ORD202602040002', 4, 7, 99.00, 0, 3, NULL, 0, NULL, 0, '2026-01-24 17:05:00'),
  (8, 'ORD202602020002', 5, 8, 129.00, 1, 3, '2026-02-02 12:05:00', 0, NULL, 0, '2026-01-25 12:06:00');

INSERT INTO evaluation (id, user_id, course_id, coach_id, reservation_id, score, content, status, deleted) VALUES
  (1, 2, 1, NULL, 1, 5, '课程很专业，教练讲解清晰。', 1, 0);
INSERT INTO evaluation (id, user_id, course_id, coach_id, reservation_id, score, content, status, deleted) VALUES
  (2, 4, 4, NULL, 4, 4, '动作纠错细致，提升明显。', 1, 0),
  (3, 5, 6, NULL, 5, 5, '训练强度合适，收获很大。', 1, 0);

INSERT INTO income_stat (id, stat_date, order_count, total_amount, refund_amount) VALUES
  (1, '2026-02-01', 1, 79.00, 0.00);
INSERT INTO income_stat (id, stat_date, order_count, total_amount, refund_amount) VALUES
  (2, '2026-02-02', 2, 328.00, 0.00),
  (3, '2026-02-03', 1, 199.00, 0.00),
  (4, '2026-02-04', 2, 129.00, 129.00);

INSERT INTO feedback (id, user_id, type, content, status) VALUES
  (1, 2, '建议', '希望增加晚间课程的场次。', 0);
INSERT INTO feedback (id, user_id, type, content, status, reply_content, reply_time) VALUES
  (2, 4, '问题', '预约后无法修改时间', 1, '可先取消再重新预约', '2026-02-02 12:00:00'),
  (3, 5, '建议', '希望增加早晨课程', 0, NULL, NULL);

INSERT INTO notice (id, title, content, publish_time, status) VALUES
  (1, '春节营业通知', '春节期间正常营业，部分课程时间有所调整。', '2026-02-01 08:00:00', 1);
INSERT INTO notice (id, title, content, publish_time, status) VALUES
  (2, '课程安排调整', '2月中旬课程安排略有调整，请留意公告。', '2026-02-05 09:00:00', 1),
  (3, '设备维护通知', '本周末将进行设备维护，部分场地暂停使用。', '2026-02-07 18:00:00', 0);

INSERT INTO sys_param (id, param_key, param_value, remark, status) VALUES
  (1, 'max_reserve_per_user', '3', '单个用户最多预约次数', 1),
  (2, 'cancel_before_minutes', '60', '预约开始前多少分钟允许取消', 1),
  (3, 'refund_enabled', '1', '是否启用退款模拟', 1);
INSERT INTO file_resource (id, biz_type, biz_id, file_name, file_path, file_url, content_type, file_size, uploader_id, deleted) VALUES
  (1, 'course', 1, 'course-cover-1.jpg', 'uploads/2026-02-01/course-cover-1.jpg', '/uploads/2026-02-01/course-cover-1.jpg', 'image/jpeg', 123456, 1, 0),
  (2, 'coach', 1, 'coach-avatar-1.jpg', 'uploads/2026-02-01/coach-avatar-1.jpg', '/uploads/2026-02-01/coach-avatar-1.jpg', 'image/jpeg', 223344, 1, 0);
