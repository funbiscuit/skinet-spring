create table product_brands
(
    id   serial primary key,
    name varchar(255) not null
);

create table product_types
(
    id   serial primary key,
    name varchar(255) not null
);

alter table products
    add column description varchar(255);
alter table products
    add column picture_url varchar(255);
alter table products
    add column price decimal(18, 2);
alter table products
    add column type_id integer;
alter table products
    add column brand_id integer;

alter table products
    add foreign key (type_id) references product_types (id);
alter table products
    add foreign key (brand_id) references product_brands (id);
