INSERT INTO "role"("name") VALUES('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO "role"("name") VALUES('ROLE_BETTOR') ON CONFLICT DO NOTHING;
INSERT INTO "role"("name") VALUES('ROLE_CASHIER') ON CONFLICT DO NOTHING;


DO $$
    DECLARE "roleUuid" UUID;
    BEGIN
        SELECT role.uuid FROM role WHERE "role".name = 'ROLE_ADMIN' INTO "roleUuid";
        IF "roleUuid" IS NOT NULL THEN
            INSERT INTO "user"(name, surname, login, password, role_uuid)
                VALUES ('admin', 'adminow', 'admin', '$2y$10$N.VywzorqjhnAsvsimIFUeDUCVE0nZm1Wc7YJ9LFgW/HZHOjFe/hK', "roleUuid")
                    ON CONFLICT DO NOTHING;
        END IF;

    END;$$
