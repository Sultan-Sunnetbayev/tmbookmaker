CREATE TABLE IF NOT EXISTS "racetrack"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(255) NOT NULL DEFAULT '',
    "city_uuid" UUID,
    "region_uuid" UUID,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "racetrack_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "racetrack_unique"
        UNIQUE("region_uuid", "city_uuid", "name"),
    CONSTRAINT "racetrack_city_uuid_fk"
        FOREIGN KEY("city_uuid")
            REFERENCES "city"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "racetrack_region_uuid_fk"
        FOREIGN KEY("region_uuid")
            REFERENCES "region"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "horse_race_event"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(255) NOT NULL DEFAULT '',
    "date" DATE,
    "time" TIME,
    "event_number" INT NOT NULL DEFAULT 0,
    "number_horse_races" INT NOT NULL DEFAULT 0,
    "number_bets" INT NOT NULL DEFAULT 0,
    "racetrack_uuid" UUID NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "horse_race_event_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "horse_race_event_number_horse_races_check"
        CHECK("number_horse_races">=0),
    CONSTRAINT "horse_race_event_number_bets_check"
        CHECK("number_bets">=0),
    CONSTRAINT "horse_race_event_racetrack_uuid_fk"
        FOREIGN KEY("racetrack_uuid")
            REFERENCES "racetrack"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "bet_option"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(256) NOT NULL,
    "odds" INT,
    "min_amount" NUMERIC(7, 2) NOT NULL,
    "max_amount" NUMERIC(7, 2) NOT NULL,
    "is_active" BOOLEAN  NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bet_option_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "bet_option_unique"
        UNIQUE("name", "odds", "min_amount", "max_amount"),
    CONSTRAINT "bet_option_min_amount_check"
        CHECK("min_amount">0.00),
    CONSTRAINT "bet_option_max_amount_check"
        CHECK("max_amount">0.00)
);


CREATE TABLE IF NOT EXISTS "horse_race"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "date" DATE,
    "time" TIME,
    "data_file_uuid" UUID,
    "number_horses" INT NOT NULL DEFAULT 0,
    "number_bets" INT NOT NULL DEFAULT 0,
    "bet_permission" BOOLEAN NOT NULL DEFAULT TRUE,
    "horse_race_event_uuid" UUID NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "horse_race_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "horse_race_data_file_uuid_unique"
        UNIQUE("data_file_uuid"),
    CONSTRAINT "horse_races_data_file_fk"
        FOREIGN KEY("data_file_uuid")
            REFERENCES "file"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "horse_race_horse_race_event_uuid_fk"
        FOREIGN KEY("horse_race_event_uuid")
            REFERENCES "horse_race_event"("uuid")
                ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS "horse_race_bet_option"(
    "horse_race_uuid" UUID NOT NULL,
    "bet_option_uuid" UUID NOT NULL,

    CONSTRAINT "horse_race_bet_option_pk"
        PRIMARY KEY("horse_race_uuid", "bet_option_uuid"),
    CONSTRAINT "horse_race_bet_option_horse_race_uuid_fk"
        FOREIGN KEY("horse_race_uuid")
            REFERENCES "horse_race"("uuid")
                ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT "horse_race_bet_option_bet_option_uuid_fk"
        FOREIGN KEY("bet_option_uuid")
            REFERENCES "bet_option"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "horse"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "number" CHARACTER VARYING(2) NOT NULL,
    "place" INT,
    "horse_race_uuid" UUID NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "horse_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "horse_place_check"
        CHECK("place">0),
    CONSTRAINT "horse_unique"
        UNIQUE("horse_race_uuid", "number"),
    CONSTRAINT "horse_horse_race_uuid_fk"
        FOREIGN KEY("horse_race_uuid")
            REFERENCES "horse_race"("uuid")
                ON UPDATE CASCADE ON DELETE CASCADE
);
