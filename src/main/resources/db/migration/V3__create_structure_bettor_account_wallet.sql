CREATE TABLE IF NOT EXISTS "bank"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "name" CHARACTER VARYING(256) NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT TRUE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bank_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "bank_unique"
        UNIQUE("name")
);


CREATE TABLE IF NOT EXISTS "card"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "card_number" CHARACTER VARYING(16) NOT NULL,
    "holder_name" CHARACTER VARYING(50) NOT NULL,
    "cvc_code" CHARACTER VARYING(4) NOT NULL,
    "expiration_date" CHARACTER VARYING(5) NOT NULL,
    "bank_uuid" UUID NOT NULL,
    "bettor_uuid" UUID NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "card_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "card_unique"
        UNIQUE("card_number", "bank_uuid", "bettor_uuid"),
    CONSTRAINT "card_bank_uuid_fk"
         FOREIGN KEY("bank_uuid")
            REFERENCES "bank"("uuid")
                ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT "card_bettor_uuid_fk"
        FOREIGN KEY("bettor_uuid")
            REFERENCES "bettor"("uuid")
                ON UPDATE CASCADE ON DELETE CASCADE
);
