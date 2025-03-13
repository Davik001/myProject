CREATE TABLE product (
     id SERIAL PRIMARY KEY,
     name VARCHAR(100) UNIQUE NOT NULL,
     description TEXT,
     price DECIMAL(10,2) NOT NULL CHECK (price > 0)
);
