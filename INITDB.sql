CREATE TABLE IF NOT EXISTS groups(GroupID INTEGER, GroupName TEXT NOT NULL UNIQUE, GroupRank INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(GroupID AUTOINCREMENT));
CREATE TABLE IF NOT EXISTS users (UserID INTEGER, UserName TEXT NOT NULL UNIQUE, PassHash TEXT NOT NULL, Banned INTEGER DEFAULT 0, UserRank INTEGER NOT NULL DEFAULT 0, gid INTEGER, CONSTRAINT fk_1 FOREIGN KEY (gid) REFERENCES groups(GroupID), PRIMARY KEY(UserID AUTOINCREMENT));
CREATE TABLE IF NOT EXISTS allowed_commands_for_groups(id INTEGER, AllowedCommand TEXT NOT NULL, gid INTEGER NOT NULL, UNIQUE(AllowedCommand, gid), CONSTRAINT fk_2 FOREIGN KEY(gid) REFERENCES groups(GroupID), PRIMARY KEY(id AUTOINCREMENT));
CREATE TABLE IF NOT EXISTS allowed_commands_for_users(id INTEGER, AllowedCommand TEXT NOT NULL, uid INTEGER NOT NULL, UNIQUE(AllowedCommand, uid), CONSTRAINT fk_3 FOREIGN KEY(uid) REFERENCES users(UserID), PRIMARY KEY(id AUTOINCREMENT));

INSERT INTO groups(GroupName, GroupRank) VALUES('users', 0), ('helpers', 5), ('admins', 10);
INSERT INTO allowed_commands_for_groups(AllowedCommand, gid) VALUES('BAN', 3), ('UNBAN', 3), ('DELETE', 3), ('ADD', 3), ('RENAME', 3);
INSERT INTO users(UserName, PassHash, UserRank, gid) VALUES('admin', 'hashahhshah', 100, 3);
