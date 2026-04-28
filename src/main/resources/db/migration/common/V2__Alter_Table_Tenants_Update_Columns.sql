alter table jr_tenants
    rename column email to company_email;

alter table jr_tenants
drop
column admin_full_name,
    add admin_first_name varchar(255),
    add admin_last_name varchar(255);




