CREATE OR REPLACE FUNCTION add_horse_race("horse_race_time" TIME, "horse_race_date" DATE, "horse_race_data_file_uuid" UUID,
                                          "number_horses" INT, "bet_option_uuids" UUID[],
                                          "horse_race_horse_race_event_uuid" UUID) RETURNS UUID
    LANGUAGE plpgsql AS $$
DECLARE
    "horse_race_uuid_helper" UUID;
    "bet_option_uuid_helper" UUID;
BEGIN
    INSERT INTO "horse_race"("date", "time", "number_horses", "data_file_uuid", "horse_race_event_uuid")
        VALUES ("horse_race_date", "horse_race_time", "horse_race_number_horses", "horse_race_data_file_uuid",
            "horse_race_horse_race_event_uuid")
        ON CONFLICT DO NOTHING RETURNING "horse_race"."uuid" INTO "horse_race_uuid_helper";
    IF "horse_race_uuid_helper" IS NULL THEN
        RETURN NULL;
    END IF;
    FOREACH "bet_option_uuid_helper" IN ARRAY "bet_option_uuids" LOOP
        INSERT INTO "horse_race_bet_option"("horse_race_uuid", "bet_option_uuid")
            VALUES("horse_race_uuid_helper", "bet_option_uuid_helper") ON CONFLICT DO NOTHING;
    END LOOP;

    RETURN "horse_race_uuid_helper";
END$$;
