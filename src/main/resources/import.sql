INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('ADMIN');
INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('OWNER');
INSERT INTO `fc_coolpeace`.role (role_type) VALUES ('USER');

INSERT INTO fc_coolpeace.member (is_deleted, email, name, password) VALUES (0, 'testowner@test.com', 'testowner', 'pass123');
