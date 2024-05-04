DO $$BEGIN
    IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'transactiontype') THEN
        CREATE TYPE "transactiontype" AS ENUM(
            'CASH', 'CARD', 'ACCOUNT_WALLET'
            );
    END IF;
    IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'transactionstatus') THEN
        CREATE TYPE "transactionstatus" AS ENUM(
            'BET_WINNINGS_PAID', 'BET_PLACED', 'WITHDRAWAL', 'TOP_UP_DEPOSIT'
            );
    END IF;

END$$;


CREATE TABLE IF NOT EXISTS "cash_register"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "number" CHARACTER VARYING(4) NOT NULL,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "cashier_uuid" UUID,
    "populated_place_uuid" UUID,
    "city_uuid" UUID,
    "region_uuid" UUID,
    "is_removed" BOOLEAN NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "cash_register_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "cash_register_cashier_uuid_fk"
        FOREIGN KEY("cashier_uuid")
            REFERENCES "user"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "cash_register_populated_place_uuid_fk"
        FOREIGN KEY("populated_place_uuid")
            REFERENCES "populated_place"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "cash_register_city_uuid_fk"
        FOREIGN KEY("city_uuid")
            REFERENCES "city"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "cash_register_region_uuid_fk"
        FOREIGN KEY("region_uuid")
            REFERENCES "region"("uuid")
                ON UPDATE CASCADE

);


CREATE TABlE IF NOT EXISTS "money"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "amount" NUMERIC(7, 2) NOT NULL,
    "time" TIME WITH TIME ZONE NOT NULL,
    "date" TIMESTAMP WITH TIME ZONE NOT NULL,
    "transaction_type" TRANSACTIONTYPE NOT NULL,
    "transaction_status" TRANSACTIONSTATUS NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "money_pk"
        PRIMARY KEY("uuid")
);


CREATE TABLE IF NOT EXISTS "cash_transaction"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "bettor_uuid" UUID NOT NULL,
    "money_uuid" UUID NOT NULL,
    "cash_register_uuid" UUID NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "cash_transaction_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "cash_transaction_unique"
        UNIQUE("bettor_uuid", "money_uuid", "cash_register_uuid"),
    CONSTRAINT "cash_transaction_bettor_uuid_fk"
        FOREIGN KEY("bettor_uuid")
            REFERENCES "bettor"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "cash_transaction_money_uuid_fk"
        FOREIGN KEY("money_uuid")
            REFERENCES "money"("uuid")
                ON UPDATE CASCADE,
    CONSTRAINT "cash_Transaction_cash_register_uuid_fk"
        FOREIGN KEY("cash_register_uuid")
            REFERENCES "cash_register"("uuid")
                ON UPDATE CASCADE
);


CREATE TABLE IF NOT EXISTS "card_transaction"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "card_uuid" UUID NOT NULL,
    "money_uuid" UUID NOT NULL,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "card_transaction_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "card_transaction_unique"
        UNIQUE("card_uuid", "money_uuid"),
    CONSTRAINT "card_transaction_card_uuid_fk"
        FOREIGN KEY("card_uuid")
            REFERENCES "card"("uuid")
                ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT "card_transaction_money_uuid_fk"
        FOREIGN KEY("money_uuid")
            REFERENCES "money"("uuid")
                ON UPDATE CASCADE
);
