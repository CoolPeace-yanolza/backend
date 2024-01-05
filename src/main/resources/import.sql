INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('ADMIN');
INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('OWNER');
INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('USER');

INSERT INTO fc_coolpeace.member (is_deleted, email, name, password) VALUES (0, 'testowner@test.com', 'testowner', 'pass123');
INSERT INTO fc_coolpeace.accommodation (member_id, address, name) VALUES (1, '123 Main St', 'Accommodation 1');
INSERT INTO fc_coolpeace.room (accommodation_id, room_number, price, room_type) VALUES (1, 101, 50000, 'Single'), (1, 102, 60000, 'Double'), (1, 103, 70000, 'Suite');
