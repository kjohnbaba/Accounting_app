-- this data-prod.sql contains only production-specific data.
insert into addresses(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      address_line1, address_line2, city, state, country, zip_code)
values ('2024-04-15 00:00:00', 1, false, '2024-04-15 00:00:00', 1,
        '7925 Jones Branch Dr, #3300', 'Tysons', 'Virginia', 'VA', 'United States', '22102-1234');

insert into companies(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                      title, phone, website, address_id, company_status)
values ('2024-04-15 00:00:00', 1, false, '2024-04-15 00:00:00', 1,
        'CYDEO', '+1 (652) 852-8888', 'https://www.cydeo.com', 1, 'ACTIVE');

insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, description)
values ('2024-04-09 00:00:00', 1, false, '2024-04-09 00:00:00', 1, 'Root User'),
       ('2024-04-09 00:00:00', 1, false, '2024-04-09 00:00:00', 1, 'Admin'),
       ('2024-04-09 00:00:00', 1, false, '2024-04-09 00:00:00', 1, 'Manager'),
       ('2024-04-09 00:00:00', 1, false, '2024-04-09 00:00:00', 1, 'Employee');

insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  username, password, firstname, lastname, phone, role_id, company_id, enabled)
values
-- COMPANY-1 / CYDEO / ROOT USER
('2024-04-09 00:00:00', 1, false, '2024-04-09 00:00:00', 1,
 'root@cydeo.com', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
 'Robert', 'Martin', '+1 (852) 564-5874', 1, 1, true);

