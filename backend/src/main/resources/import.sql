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
