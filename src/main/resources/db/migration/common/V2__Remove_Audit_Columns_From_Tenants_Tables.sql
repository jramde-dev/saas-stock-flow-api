alter table jr_tenants
drop
column if exists created_by,
drop
column if exists updated_by;