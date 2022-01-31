create table users
(
    id        serial primary key,
    name      varchar(255) not null,
    pass_hash varchar(255) not null,
    email     varchar(255) not null unique
);

create table addresses
(
    id         serial primary key,
    first_name varchar(255),
    last_name  varchar(255),
    street     varchar(255),
    city       varchar(255),
    state      varchar(255),
    zip_code   varchar(255),
    country    varchar(255),
    user_id    integer not null
);

alter table addresses
    add foreign key (user_id) references users (id)
        on delete cascade;

create table roles
(
    id   serial primary key,
    name varchar(255) not null unique
);

create table user_roles
(
    user_id integer not null,
    role_id integer not null
);

alter table user_roles
    add foreign key (user_id) references users (id)
        on delete cascade;

alter table user_roles
    add foreign key (role_id) references roles (id)
        on delete cascade;

alter table user_roles
    add primary key (user_id, role_id);
