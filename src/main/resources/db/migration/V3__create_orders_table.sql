CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    order_status VARCHAR(20) NOT NULL CHECK (order_status IN ('NEW', 'PROCESSING', 'COMPLETED', 'CANCELED')),
    customer_id INT NOT NULL,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);
