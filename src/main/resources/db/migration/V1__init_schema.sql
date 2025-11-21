CREATE TABLE owners (
                        id SERIAL PRIMARY KEY,
                        full_name VARCHAR(100) NOT NULL,
                        created_at TIMESTAMP
);

CREATE TABLE cars
(
    id            SERIAL PRIMARY KEY,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    owner_id      INTEGER REFERENCES owners (id) ON DELETE RESTRICT,
    created_at    TIMESTAMP
);

CREATE TABLE parking_spots (
                               id SERIAL PRIMARY KEY,
                               spot_number VARCHAR(10) UNIQUE NOT NULL,
                               is_available BOOLEAN DEFAULT true,
                               created_at TIMESTAMP
);

CREATE TABLE reservations (
                              id SERIAL PRIMARY KEY,
                              car_id INTEGER REFERENCES cars(id) ON DELETE SET NULL,
                              spot_id INTEGER REFERENCES parking_spots(id) ON DELETE SET NULL,
                              start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              end_time TIMESTAMP,
                              is_paid BOOLEAN DEFAULT false,
                              status VARCHAR(20) DEFAULT 'ACTIVE',
                              created_at TIMESTAMP
);