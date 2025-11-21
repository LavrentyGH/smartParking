INSERT INTO owners (full_name, created_at) VALUES
                                   ('Иванов Иван Иванович', CURRENT_TIMESTAMP),
                                   ('Петрова Анна Сергеевна', CURRENT_TIMESTAMP),
                                   ('Сидоров Алексей Владимирович', CURRENT_TIMESTAMP);

INSERT INTO cars (license_plate, owner_id, created_at) VALUES
                                               ('А123ВС77', 1, CURRENT_TIMESTAMP),
                                               ('В456ОР77', 2, CURRENT_TIMESTAMP),
                                               ('С789ТК77', 3, CURRENT_TIMESTAMP);

INSERT INTO parking_spots (spot_number, created_at) VALUES
                                            ('A-01', CURRENT_TIMESTAMP),
                                            ('A-02', CURRENT_TIMESTAMP),
                                            ('A-03', CURRENT_TIMESTAMP),
                                            ('A-04', CURRENT_TIMESTAMP),
                                            ('A-05', CURRENT_TIMESTAMP);