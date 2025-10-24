-- Modify new orders columns to have sensible defaults
ALTER TABLE orders
MODIFY COLUMN total_money FLOAT DEFAULT 0,
MODIFY COLUMN shipping_method VARCHAR(100),
MODIFY COLUMN tracking_number VARCHAR(100),
MODIFY COLUMN payment_method VARCHAR(100),
MODIFY COLUMN shipping_address VARCHAR(200) DEFAULT '',
MODIFY COLUMN shipping_date DATE,
MODIFY COLUMN active TINYINT(1) DEFAULT 1;

-- Update existing rows in production to avoid NULL issues
UPDATE orders
SET 
    total_money = 0,
    shipping_method = '',
    tracking_number = '',
    payment_method = '',
    shipping_address = '',    
    active = 1
WHERE 
    total_money IS NULL
   OR shipping_method IS NULL
   OR tracking_number IS NULL
   OR payment_method IS NULL
   OR shipping_address IS NULL
   OR active IS NULL;