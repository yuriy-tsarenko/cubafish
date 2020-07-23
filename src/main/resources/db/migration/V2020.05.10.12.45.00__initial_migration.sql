
CREATE TABLE public.user_detail
(
    id         serial8 NOT NULL,
    user_name  varchar(30) UNIQUE,
    password   varchar(100) UNIQUE,
    email      varchar(50),
    contact    varchar(20),
    user_role  varchar (15),
    active boolean,
    PRIMARY KEY (id)
);

CREATE TABLE public.product
(
    id            serial8 NOT NULL,
    product_category  varchar(50),
    product_sub_category  varchar(50),
    product_brand varchar(50),
    type_of_purpose varchar(50),
    description   varchar(100),
    specification varchar(400),
    total_amount int4,
    product_price decimal,
    product_image_name varchar(300),
    PRIMARY KEY (id)
);

CREATE TABLE public.booking_list
(
    id            serial8 NOT NULL,
    product_id    int8    NOT NULL,
    first_name    varchar(20),
    middle_name   varchar(20),
    last_name     varchar(20),
    date_of_birth date,
    contact       varchar(30),
    delivery_type varchar(30),
    address       varchar(100),
    PRIMARY KEY (id)
);

CREATE TABLE public.booking_data_base
(
    id            serial8 NOT NULL,
    product_id    int8    NOT NULL,
    first_name    varchar(20),
    middle_name   varchar(20),
    last_name     varchar(20),
    date_of_birth date,
    contact       varchar(30),
    delivery_type varchar(30),
    address       varchar(100),
    PRIMARY KEY (id)
);





