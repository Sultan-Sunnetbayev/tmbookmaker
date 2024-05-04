CREATE OR REPLACE FUNCTION change_racetrack_region() RETURNS TRIGGER
    LANGUAGE plpgsql AS $$BEGIN

    UPDATE "racetrack" SET "racetrack"."region_uuid" = NEW."region_uuid" WHERE "racetrack"."city_uuid" = NEW."uuid";

    RETURN NEW;
END$$;

CREATE OR REPLACE TRIGGER "after_change_city_region"
    AFTER UPDATE ON "city"
        FOR EACH ROW EXECUTE PROCEDURE change_racetrack_region()
