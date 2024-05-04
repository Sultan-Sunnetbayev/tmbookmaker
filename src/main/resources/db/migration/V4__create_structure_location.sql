CREATE TABLE IF NOT EXISTS "region"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(20) NOT NULL ,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "region_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "region_unique"
        UNIQUE("name")
);


CREATE TABLE IF NOT EXISTS "city"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(56) NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "region_uuid" UUID NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "city_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "city_unique"
        UNIQUE("region_uuid", "name"),
    CONSTRAINT "city_region_uuid_fk"
        FOREIGN KEY("region_uuid")
            REFERENCES "region"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "populated_place"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(126) NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "city_uuid" UUID NOT NULL,
    "region_uuid" UUID NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "populated_place_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "populated_place_unique"
        UNIQUE("region_uuid", "city_uuid", "name"),
    CONSTRAINT "populated_place_city_uuid_fk"
        FOREIGN KEY("city_uuid")
            REFERENCES "city"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "populated_place_region_uuid_fk"
        FOREIGN KEY("region_uuid")
            REFERENCES "region"("uuid")
                ON UPDATE CASCADE
);
