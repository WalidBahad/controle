-- Create cars table if it doesn't exist
CREATE TABLE IF NOT EXISTS cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    car_year INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    price_per_day DOUBLE NOT NULL
    );


