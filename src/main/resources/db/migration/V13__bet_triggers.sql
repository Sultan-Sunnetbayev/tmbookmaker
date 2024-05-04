CREATE OR REPLACE FUNCTION increment_horse_race_number_bets() RETURNS TRIGGER AS
$$BEGIN

    UPDATE "horse_race" SET "horse_race"."number_bets" = "horse_race"."number_bets" + 1
        WHERE "horse_race"."uuid" = NEW."horse_race_uuid";
    UPDATE "horse_race_event" SET "horse_race_event"."number_bets" = "horse_race_event"."number_bets" + 1
        WHERE "horse_race_event"."uuid" = (SELECT "horse_race"."horse_race_event_uuid" FROM "horse_race"
                                                WHERE "horse_race"."uuid" = NEW."horse_race_uuid");

    RETURN NEW;
END$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER "after_insert_bet_increment_horse_race_number_bets"
    AFTER INSERT ON "bet"
        FOR EACH ROW EXECUTE PROCEDURE increment_horse_race_number_bets();
