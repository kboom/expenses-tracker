insert into authorities (authority_name) VALUES ('ROLE_ADMIN');
insert into authorities (authority_name) VALUES ('ROLE_MANAGER');
insert into authorities (authority_name) VALUES ('ROLE_USER');

insert into users (id, username, password, email, enabled, last_pwd_rst_dt) VALUES (100, 'admin', '$2a$10$Q4./1dZmvxV1.NvAhvRBPejp/4ufGOcfF1YhgeCNHleUz7dr.SiTK', 'admin@test.com', true, '2000-01-01');
insert into user_authorities (user_id, authority_name) VALUES (100, 'ROLE_ADMIN');

insert into users (id, username, password, email, enabled, last_pwd_rst_dt) VALUES (101, 'manager', '$2a$10$Q4./1dZmvxV1.NvAhvRBPejp/4ufGOcfF1YhgeCNHleUz7dr.SiTK', 'manager@test.com', true, '2000-01-01');
insert into user_authorities (user_id, authority_name) VALUES (101, 'ROLE_MANAGER');