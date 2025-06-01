CREATE TABLE users(
id uuid primary key default gen_random_uuid(),
number_phone varchar (11) not null unique,
email varchar(255) not  null  unique ,
password varchar(100) not  null ,
firstname varchar(30) not null ,
created_at timestamp with time zone not null default now()
);

CREATE INDEX  idx_users_email on users(email);
CREATE INDEX idx_users_number_phone on users(number_phone);





