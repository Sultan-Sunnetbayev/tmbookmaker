CREATE OR REPLACE FUNCTION edit_region("region_uuid" UUID, "region_name" VARCHAR) RETURNS BOOLEAN
    LANGUAGE plpgsql AS $$
DECLARE
    "is_edited" BOOLEAN;
BEGIN
    "is_edited" = FALSE;
    IF (SELECT CASE WHEN EXISTS(SELECT 1 FROM "region" WHERE ("region"."uuid" <> "region_uuid") AND
                                    (LOWER("region"."name") = LOWER("region_name")))THEN TRUE ELSE FALSE END) THEN
        RETURN "is_edited";
    END IF;

    UPDATE "region" SET "region"."name" = "region_name" WHERE "region"."uuid" = "region_uuid" RETURNING TRUE INTO "is_edited";

    RETURN "is_edited";
END$$;


CREATE OR REPLACE FUNCTION edit_city("city_uuid" UUID, "city_name" VARCHAR, "region_uuid" UUID) RETURNS BOOLEAN
    LANGUAGE plpgsql AS $$
DECLARE
    "is_edited" BOOLEAN;
BEGIN
    "is_edited" = FALSE;
    IF (SELECT CASE WHEN EXISTS(
            SELECT 1 FROM "city" WHERE ("city"."uuid" <> "city_uuid") AND (LOWER("city"."name") = LOWER("city_name")) AND
                ("city"."region_uuid" = "region_uuid")
    )THEN TRUE ELSE FALSE END) THEN
        RETURN "is_edited";
    END IF;

    UPDATE "city" SET "city"."name" = "city_name", "city"."region_uuid" = "region_uuid" WHERE ("city"."uuid" = "city_uuid")
        RETURNING TRUE INTO "is_edited";

    RETURN "is_edited";
END$$;


CREATE OR REPLACE FUNCTION edit_racetrack("racetrack_uuid" UUID, "racetrack_name" VARCHAR, "city_uuid" UUID, "region_uuid" UUID)
    RETURNS BOOLEAN LANGUAGE plpgsql AS $$
DECLARE
    "is_edited" BOOLEAN;
BEGIN
    "is_edited" = FALSE;
    IF (SELECT CASE WHEN EXISTS(
            SELECT 1 FROM "racetrack" WHERE ("racetrack"."uuid" <> "racetrack_uuid") AND
                (LOWER("racetrack"."name") = LOWER("racetrack_name")) AND ("racetrack"."city_uuid" = "city_uuid") AND
                ("racetrack"."region_uuid" = "region_uuid")
    )THEN TRUE ELSE FALSE END) THEN
        RETURN "is_edited";
    END IF;

    UPDATE "racetrack" SET "racetrack"."name" = "racetrack_name", "racetrack"."city_uuid" = "city_uuid",
        "racetrack"."region_uuid" = "region_uuid" WHERE ("racetrack"."uuid" = "racetrack_uuid") RETURNING TRUE INTO "is_edited";

    RETURN "is_edited";
END$$;
