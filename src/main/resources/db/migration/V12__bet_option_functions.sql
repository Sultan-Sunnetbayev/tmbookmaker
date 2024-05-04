CREATE OR REPLACE FUNCTION edit_bet_option("bet_option_uuid" UUID, "bet_option_name" VARCHAR, "bet_option_min_amount" NUMERIC(7, 2),
                                           "bet_option_max_amount" NUMERIC(7,2))
    RETURNS BOOLEAN LANGUAGE plpgsql AS $$
DECLARE
    "is_edited" BOOLEAN;
BEGIN
    "is_edited"=FALSE;

    IF (SELECT CASE WHEN EXISTS(
            SELECT 1 FROM "bet_option" WHERE ("bet_option"."uuid" <> "bet_option_uuid") AND
                ((LOWER("bet_option"."name") = LOWER("bet_option_name")) AND
                ("bet_option"."min_amount"="bet_option_min_amount") AND
                ("bet_option"."max_amount"="bet_option_max_amount"))
       )THEN TRUE ELSE FALSE END) THEN
            RETURN "is_edited";
    END IF;

    UPDATE "bet_option" SET "bet_option"."name" = "bet_option_name", "bet_option"."min_amount" = "bet_option_min_amount",
        "bet_option"."max_amount" = "bet_option_max_amount" WHERE "bet_option"."uuid" = "bet_option_uuid"
        RETURNING TRUE INTO "is_edited";

    RETURN "is_edited";
END$$;
