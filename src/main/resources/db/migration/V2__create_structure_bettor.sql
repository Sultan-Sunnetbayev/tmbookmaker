CREATE TABLE IF NOT EXISTS "bettor"(
    "uuid" UUID NOT NULL DEFAULT UUID_GENERATE_V4(),
    "username" CHARACTER VARYING(255) NOT NULL DEFAULT '',
    "password" CHARACTER VARYING(256) NOT NULL,
    "phone_number" CHARACTER VARYING(12) NOT NULL,
    "deposit" NUMERIC(7, 2) NOT NULL DEFAULT 0.00,
    "winnings" NUMERIC(7, 2) NOT NULL DEFAULT 0.00,
    "cash_out" NUMERIC(7, 2) NOT NULL DEFAULT 0.00,
    "role_uuid" UUID NOT NULL,
    "activation_code" VARCHAR(4),
    "limit_sms" INT NOT NULL DEFAULT 0,
    "confirm_age_18" BOOLEAN,
    "acceptance_privacy_policy" BOOLEAN,
    "is_registered" BOOLEAN NOT NULL DEFAULT FALSE,
    "is_active" BOOLEAN NOT NULL DEFAULT FALSE,
    "created" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "bettor_uuid_pk"
        PRIMARY KEY("uuid"),
    CONSTRAINT "bettor_unique"
        UNIQUE("phone_number"),
    CONSTRAINT "bettor_limit_sms_check"
        CHECK("limit_sms">=0),
    CONSTRAINT "bettor_confirm_age_18_check"
        CHECK("confirm_age_18" = TRUE),
    CONSTRAINT "bettor_acceptance_privacy_policy_check"
        CHECK("acceptance_privacy_policy" = TRUE),
    CONSTRAINT "bettor_role_uuid_fk"
        FOREIGN KEY("role_uuid")
            REFERENCES "role"("uuid")
                ON UPDATE CASCADE
);
