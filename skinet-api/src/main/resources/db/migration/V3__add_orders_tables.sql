create table delivery_method
(
    id            serial primary key,
    short_name    varchar(255) not null,
    delivery_time varchar(255) not null,
    description   varchar(255) not null,
    price         varchar(255) not null
);

create table orders
(
    id                 serial primary key,
    buyer_email        varchar(255)   not null,
    order_date         timestamp      not null,

    ship_to_first_name varchar(255)   not null,
    ship_to_last_name  varchar(255)   not null,
    ship_to_street     varchar(255)   not null,
    ship_to_city       varchar(255)   not null,
    ship_to_state      varchar(255)   not null,
    ship_to_zip_code   varchar(255)   not null,
    ship_to_country    varchar(255)   not null,

    delivery_method_id integer        not null,
    subtotal           decimal(18, 2) not null,
    status             varchar(64)    not null,
    payment_intent_id  varchar(255),

    foreign key (delivery_method_id) references delivery_method (id) on delete restrict
);

create table order_item
(
    id           serial primary key,

    product_id   integer,
    product_name varchar(255)   not null,
    picture_url  varchar(255)   not null,

    price        decimal(18, 2) not null,
    quantity     integer        not null,
    order_id     integer        not null,

    foreign key (product_id) references products (id) on delete restrict,
    foreign key (order_id) references orders (id) on delete cascade
);
