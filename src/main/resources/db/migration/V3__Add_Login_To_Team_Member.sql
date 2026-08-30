ALTER TABLE team_member
    ADD COLUMN username VARCHAR(100),
    ADD COLUMN password_hash VARCHAR(255);

UPDATE team_member
SET username = name;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE team_member
SET password_hash = crypt(name || '@123', gen_salt('bf', 12));

ALTER TABLE team_member
    ALTER COLUMN username SET NOT NULL,
    ALTER COLUMN password_hash SET NOT NULL;

CREATE UNIQUE INDEX uq_team_member_username ON team_member(username);

