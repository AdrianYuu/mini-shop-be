ALTER TABLE order_items
    ADD CONSTRAINT uc_order_product
        UNIQUE (order_id, product_id);