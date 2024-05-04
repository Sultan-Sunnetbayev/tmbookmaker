DO $$BEGIN
    IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'betstatustype') THEN
        CREATE TYPE "betstatustype" AS ENUM(
            'WIN', 'LOSE', 'PENDING'
            );
    END IF;

END$$;


CREATE TABLE IF NOT EXISTS "bet"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "bet_option_uuid" UUID NOT NULL,
    "horse_race_uuid" UUID NOT NULL,
    "bettor_uuid" UUID NOT NULL,
    "transacted_money_uuid" UUID,
    "odds" INT NOT NULL,
    "status" BETSTATUSTYPE NOT NULL,
    "winnings" NUMERIC(7, 2) ,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bet_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "bet_unique"
        UNIQUE("bet_option_uuid", "horse_race_uuid", "bettor_uuid"),
    CONSTRAINT "bet_bet_option_uuid_fk"
        FOREIGN KEY("bet_option_uuid")
            REFERENCES "bet_option"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "bet_horse_race_uuid_fk"
        FOREIGN KEY("horse_race_uuid")
            REFERENCES "horse_race"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "bet_bettor_uuid_fk"
        FOREIGN KEY("bettor_uuid")
            REFERENCES "bettor"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "bet_money_uuid_fk"
        FOREIGN KEY("transacted_money_uuid")
            REFERENCES "money"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "bet_horse"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "bet_uuid" UUID NOT NULL,
    "horse_uuid" UUID NOT NULL,
    "place" INT NOT NULL,
    "is_correct" BOOLEAN DEFAULT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bet_horse_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "bet_horse_horse_uuid_fk"
        FOREIGN KEY("horse_uuid")
            REFERENCES "horse"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "bet_horse_bet_uuid_fk"
        FOREIGN KEY("bet_uuid")
            REFERENCES "bet"("uuid")
                ON UPDATE CASCADE ON DELETE CASCADE
);
