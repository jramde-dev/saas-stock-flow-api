ALTER TABLE jr_categories
    ADD tenant_id varchar(255);

ALTER TABLE jr_products
    ADD tenant_id varchar(255);

ALTER TABLE jr_stock_mvmts
    ADD tenant_id varchar(255);

COMMENT
ON COLUMN jr_categories.tenant_id IS 'L''iddentifiant du tenant';

COMMENT
ON COLUMN jr_products.tenant_id IS 'L''iddentifiant du tenant';

COMMENT
ON COLUMN jr_stock_mvmts.tenant_id IS 'L''iddentifiant du tenant';