CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS "role"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(50),
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "role_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "role_unique"
        UNIQUE("name")
);


CREATE TABLE IF NOT EXISTS "user"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(50) NOT NULL,
    "surname" CHARACTER VARYING(75) NOT NULL,
    "login" CHARACTER VARYING(321) NOT NULL,
    "password" CHARACTER VARYING(256) NOT NULL,
    "role_uuid" UUID NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "user_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "user_unique"
        UNIQUE("login"),
    CONSTRAINT "user_role_uuid_fk"
        FOREIGN KEY("role_uuid")
            REFERENCES "role"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "file"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(256),
    "path" CHARACTER VARYING(300) NOT NULL,
    "extension" CHARACTER VARYING(50),
    "size" BIGINT NOT NULL DEFAULT 0,
    "is_confirmed" BOOLEAN NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "file_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "file_unique"
        UNIQUE("path")
);