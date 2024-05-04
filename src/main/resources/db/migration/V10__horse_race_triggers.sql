CREATE OR REPLACE FUNCTION increment_and_confirm_horse_race_data() RETURNS TRIGGER AS
$$BEGIN
    IF NEW."data_file_uuid" IS NOT NULL THEN
        UPDATE "file" SET "file"."is_confirmed" = TRUE WHERE "file"."uuid" = NEW."data_file_uuid";
    END IF;

    UPDATE "horse_race_event" SET "horse_race_event"."number_horse_races" = "horse_race_event"."number_horse_races" + 1
                                                WHERE "horse_race_event"."uuid" = NEW."horse_race_event_uuid";

    RETURN NEW;
END$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER "after_insert_horse_race_increment_and_confirm_horse_race_data"
    AFTER INSERT ON "horse_race"
        FOR EACH ROW EXECUTE PROCEDURE increment_and_confirm_horse_race_data();


CREATE OR REPLACE FUNCTION increment_horse_race_event_numbers() RETURNS TRIGGER AS
$$BEGIN

    UPDATE "horse_race_event" SET "horse_race_event"."event_number" = "horse_race_event"."horse_race_event"."event_number" + 1
        WHERE "horse_race_event"."racetrack_uuid" = NEW."racetrack_uuid";

    RETURN NEW;
END$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER "after_insert_horse_race_event_increment_event_number"
    AFTER INSERT ON "horse_race_event"
        FOR EACH ROW EXECUTE PROCEDURE increment_horse_race_event_numbers();
