INSERT INTO "bet_option"(name, odds, min_amount, max_amount, is_active)
                                                      VALUES('2-AT OÝNY', 2, 5.00, 100.00, TRUE),
                                                            ('3-AT OÝNY', 5, 5.00, 200.00, TRUE) ON CONFLICT DO NOTHING;