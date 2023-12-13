INSERT INTO Role (name) VALUES ('Basic');
INSERT INTO Role (name) VALUES ('Advanced');

INSERT INTO Role_Permissions (role_id, permissions) VALUES (1, 'VOTESKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (1, 'ADD_SONG');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (1, 'HISTORY_SEARCH');

INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'PAUSE_PLAY');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'CHANGE_VOLUME');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'CHANGE_ORDER');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'DELETE_SONGS');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'MANAGE_ROLES');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'MANAGE_USER');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'SKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'VOTESKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'ADD_SONG');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'HISTORY_SEARCH');

/* Password is 'Passwort{id}!' */
INSERT INTO User_Info (id, username, password, email, verified) VALUES (1, 'Markus', '$2a$10$YFqbWQmdKvSNBih6B9.Mbu7ckdxUuSaqRtiFTtFQaOE9Qt57sEwmy', 'user1@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (2, 'Daniel', '$2a$10$Xfe5nPCrBVUUIVicrKzsQ.18lAmt/ZvMwpFFBPfG6.LZxhv6reqCe', 'user2@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (3, 'Luki', '$2a$10$NGu3EeUX24steDEI7iYqaOmRjyX/rJ9kNGFXJl1Ylu2PACxj7O9Hq', 'user3@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (4, 'Eyüp', '$2a$10$wPLZYagc9LV7Tc8TGUNqGe8bVDExpIEC1g8wnNgwR4bVgo4Qfe4/i', 'user4@email.com', false);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (5, 'Toni', '$2a$10$Qwc8qAQhlF1HLurcZxFrs.HuG3TdgmAQ8GEKV.gK3U6AhLwC9yzr6', 'user5@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (6, 'Nico Zach', '$2a$10$6533aj/775lPPHE4ocFkR.bBa7qmzmb/s7YsdHeqzNYyrEoJFVlkW', 'user6@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (7, 'Philip', '$2a$10$RzLNds0LUeWhj/6iVmuV1eghHrSBYop4c61pAwB17MeTTD4zSeGk2', 'user7@email.com', false);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (8, 'Dren', '$2a$10$IoTsE6ZunQifJvToOdF/..9fo03k69.rwAc7XrTf80GvSaPw9085.', 'user8@email.com', false);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (9, 'Sebi', '$2a$10$Yz0clBEC6oiQDRjyv/RQwuzKwsLGwvCW056BQrr.h65flpXpXI0oS', 'user9@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (10, 'Markus II', '$2a$10$AaO3ECNtb4hFpIxyUbZdCOJ/7o1hO1P7h1mWetUxxoLQYVUMUWp7C', 'user10@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (11, 'Nils', '$2a$10$ijVxL4C2mepTrNhwsK3a9OMhNw7iZUa/H6i5J6gfkiUhBhtGVFsA6', 'user11@email.com', false);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (12, 'Zyprian', '$2a$10$9sv5HYHDnEcCGkB78v/d0.SZhNFd81mC0S3ZW.0plixQ2tOwIT3Ju', 'user12@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (13, 'Severin', '$2a$10$nYypNpvRlADznvsiAjXqf.JV2dz91VSsSIHX2gqWgl2SE/ulfmfUm', 'user13@email.com', false);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (14, 'Raphi', '$2a$10$I8CWWFgiqpxUzIyYg5rf2uGFo4QrxzumrSBy.yMi8Bv54etVh.4He', 'user14@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (15, 'Luki Linke', '$2a$10$H5iDbxIwPUayCAsS8EIBfu64GHjkRpmayUO9qJyX3tjYVMt8JalM2', 'user15@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (16, 'Nina', '$2a$10$CkrTJOzdT8d/U8FIayCTCO2iIR36TiBS1QKgmrMlmLnQ0o2cWpY62', 'user16@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (17, 'Laurenz', '$2a$10$5pxVMFFSJakNoTs2A2LEJucVSWsqTHAhCfeOaMhgd8VwjFFiUWCTq', 'user17@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (18, 'Lorenz', '$2a$10$7z/KbRbtfPMkUmG4sORKPOxuNsMHQpCVByQS4qV7m5ed9m06wAdPG', 'user18@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (19, 'Benji', '$2a$10$N6/M.L2qqPjmuIx9.6Gsn.SWXrGe5/EBWsaSw2KxETkfwEDo5/2Q6', 'user19@email.com', true);
INSERT INTO User_Info (id, username, password, email, verified) VALUES (20, 'Steffen Hofmann', '$2a$10$IVD6dLTkCpW5vvGVR6egUuRsgEmnu01LuWq4fFNkS9kyewkLqZ6Hq', 'user20@email.com', true);

INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (2, 1);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (2, 2);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (2, 3);

INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 1);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 2);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 3);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 4);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 5);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 6);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 7);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 8);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 9);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 10);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 11);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 12);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 13);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 14);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 15);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 16);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 17);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 18);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 19);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (1, 20);