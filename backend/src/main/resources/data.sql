INSERT INTO Role (name) VALUES ('Basic');
INSERT INTO Role (name) VALUES ('Advanced');
INSERT INTO Role (name) VALUES ('Owner');

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
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'REPLAY');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'VOTESKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'ADD_SONG');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'HISTORY_SEARCH');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (2, 'MANAGE_SYSTEM_SETTINGS');

INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'PAUSE_PLAY');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'CHANGE_VOLUME');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'CHANGE_ORDER');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'DELETE_SONGS');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'MANAGE_ROLES');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'MANAGE_USER');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'SKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'REPLAY');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'VOTESKIP');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'ADD_SONG');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'HISTORY_SEARCH');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'MANAGE_SYSTEM_SETTINGS');

/* Password is 'Passwort{id}!' */
INSERT INTO User_Info (username, password, email, verified) VALUES ('Markus', '$2a$10$YFqbWQmdKvSNBih6B9.Mbu7ckdxUuSaqRtiFTtFQaOE9Qt57sEwmy', 'user1@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Daniel', '$2a$10$Xfe5nPCrBVUUIVicrKzsQ.18lAmt/ZvMwpFFBPfG6.LZxhv6reqCe', 'user2@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Luki', '$2a$10$NGu3EeUX24steDEI7iYqaOmRjyX/rJ9kNGFXJl1Ylu2PACxj7O9Hq', 'user3@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Eyüp', '$2a$10$wPLZYagc9LV7Tc8TGUNqGe8bVDExpIEC1g8wnNgwR4bVgo4Qfe4/i', 'user4@email.com', false);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Toni', '$2a$10$Qwc8qAQhlF1HLurcZxFrs.HuG3TdgmAQ8GEKV.gK3U6AhLwC9yzr6', 'user5@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Nico Zach', '$2a$10$6533aj/775lPPHE4ocFkR.bBa7qmzmb/s7YsdHeqzNYyrEoJFVlkW', 'user6@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Philip', '$2a$10$RzLNds0LUeWhj/6iVmuV1eghHrSBYop4c61pAwB17MeTTD4zSeGk2', 'user7@email.com', false);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Dren', '$2a$10$IoTsE6ZunQifJvToOdF/..9fo03k69.rwAc7XrTf80GvSaPw9085.', 'user8@email.com', false);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Sebi', '$2a$10$Yz0clBEC6oiQDRjyv/RQwuzKwsLGwvCW056BQrr.h65flpXpXI0oS', 'user9@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Markus II', '$2a$10$AaO3ECNtb4hFpIxyUbZdCOJ/7o1hO1P7h1mWetUxxoLQYVUMUWp7C', 'user10@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Nils', '$2a$10$ijVxL4C2mepTrNhwsK3a9OMhNw7iZUa/H6i5J6gfkiUhBhtGVFsA6', 'user11@email.com', false);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Zyprian', '$2a$10$9sv5HYHDnEcCGkB78v/d0.SZhNFd81mC0S3ZW.0plixQ2tOwIT3Ju', 'user12@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Severin', '$2a$10$nYypNpvRlADznvsiAjXqf.JV2dz91VSsSIHX2gqWgl2SE/ulfmfUm', 'user13@email.com', false);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Raphi', '$2a$10$I8CWWFgiqpxUzIyYg5rf2uGFo4QrxzumrSBy.yMi8Bv54etVh.4He', 'user14@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Luki Linke', '$2a$10$H5iDbxIwPUayCAsS8EIBfu64GHjkRpmayUO9qJyX3tjYVMt8JalM2', 'user15@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Nina', '$2a$10$CkrTJOzdT8d/U8FIayCTCO2iIR36TiBS1QKgmrMlmLnQ0o2cWpY62', 'user16@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Laurenz', '$2a$10$5pxVMFFSJakNoTs2A2LEJucVSWsqTHAhCfeOaMhgd8VwjFFiUWCTq', 'user17@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Lorenz', '$2a$10$7z/KbRbtfPMkUmG4sORKPOxuNsMHQpCVByQS4qV7m5ed9m06wAdPG', 'user18@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Benji', '$2a$10$N6/M.L2qqPjmuIx9.6Gsn.SWXrGe5/EBWsaSw2KxETkfwEDo5/2Q6', 'user19@email.com', true);
INSERT INTO User_Info (username, password, email, verified) VALUES ('Steffen Hofmann', '$2a$10$IVD6dLTkCpW5vvGVR6egUuRsgEmnu01LuWq4fFNkS9kyewkLqZ6Hq', 'user20@email.com', true);

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

INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (2, 1);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (2, 2);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (2, 3);

INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 1);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 2);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 3);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 4);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 5);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 6);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 7);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 8);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 9);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 10);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 11);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 12);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 13);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 14);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 15);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 16);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 17);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 18);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 19);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (1, 20);

--ChatGPT-Testdata

-- Role 3: The Silent Voter
INSERT INTO Role (name) VALUES ('The Silent Voter');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (3, 'VOTESKIP');

-- Role 4: Audio Engineer
INSERT INTO Role (name) VALUES ('Audio Engineeeeeeeeeeeeeeeeeeeeeeeeer');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (4, 'CHANGE_VOLUME');

-- Role 5: Song Enthusiast
INSERT INTO Role (name) VALUES ('Song Enthusiast');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (5, 'ADD_SONG');

-- Role 6: Role Manager
INSERT INTO Role (name) VALUES ('Role Manager');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (6, 'MANAGE_ROLES');

-- Role 7: Skipper
INSERT INTO Role (name) VALUES ('Skipper');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (7, 'SKIP');

-- Role 8: Song Deleter
INSERT INTO Role (name) VALUES ('Song Deleter');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (8, 'DELETE_SONGS');

-- Role 9: History Explorer
INSERT INTO Role (name) VALUES ('History Explorer');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (9, 'HISTORY_SEARCH');

-- Role 10: Playback Maestro
INSERT INTO Role (name) VALUES ('Playback Maestro');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (10, 'PAUSE_PLAY');
INSERT INTO Role_Permissions (role_id, permissions) VALUES (10, 'CHANGE_ORDER');

-- Users for Role 3
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (3, 2);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (3, 5);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (3, 12);

-- Users for Role 4
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (4, 8);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (4, 15);

-- Users for Role 5
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (5, 3);

-- Users for Role 6
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (6, 7);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (6, 10);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (6, 18);

-- Users for Role 7
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (7, 1);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (7, 14);

-- Users for Role 8
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (8, 9);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (8, 13);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (8, 17);

-- Users for Role 9
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (9, 6);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (9, 20);

-- Users for Role 10
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (10, 4);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (10, 11);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (10, 16);
INSERT INTO User_Info_Roles (roles_id, user_info_entity_id) VALUES (10, 19);

-- Users for Role 3
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (3, 2);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (3, 5);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (3, 12);

-- Users for Role 4
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (4, 8);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (4, 15);

-- Users for Role 5
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (5, 3);

-- Users for Role 6
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (6, 7);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (6, 10);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (6, 18);

-- Users for Role 7
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (7, 1);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (7, 14);

-- Users for Role 8
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (8, 9);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (8, 13);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (8, 17);

-- Users for Role 9
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (9, 6);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (9, 20);

-- Users for Role 10
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (10, 4);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (10, 11);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (10, 16);
INSERT INTO ROLE_MEMBERS (role_id, MEMBERS_ID) VALUES (10, 19);
