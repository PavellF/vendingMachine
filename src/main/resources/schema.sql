CREATE TABLE materials_warehouse (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(64) NOT NULL UNIQUE,
    materials_left INTEGER NOT NULL,
    max_amount INTEGER NOT NULL
);

CREATE TABLE coffee (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(64) NOT NULL UNIQUE
); 

CREATE TABLE cofee_material (
    id BIGSERIAL PRIMARY KEY,
    amount_needed INTEGER NOT NULL,
    coffee_id INTEGER NOT NULL REFERENCES coffee(id),
    materials_warehouse_id INTEGER NOT NULL REFERENCES materials_warehouse(id)
); 

