CREATE UNIQUE INDEX unique_active_product_category_name
ON product_categories((CASE WHEN deleted_at IS NULL THEN name ELSE NULL END));