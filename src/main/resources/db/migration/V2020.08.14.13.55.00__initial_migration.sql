DROP TABLE IF EXISTS public.booking_list CASCADE;
DROP TABLE IF EXISTS public.booking_data_base CASCADE;

CREATE TABLE public.booking_list
(
    id                serial8 NOT NULL,
    date_of_booking   date,
    first_name        varchar(100),
    middle_name       varchar(100),
    last_name         varchar(100),
    email             varchar(100),
    contact           varchar(50),
    user_confirmation varchar(200),
    totalPrice        decimal,
    total_amount      int4,
    payment_type      varchar(50),
    delivery_type     varchar(50),
    region            varchar(200),
    city              varchar(200),
    address           varchar(200),
    booking_items     text,
    PRIMARY KEY (id)
);

CREATE TABLE public.booking_data_base
(
    id                serial8 NOT NULL,
    date_of_booking   date,
    first_name        varchar(100),
    middle_name       varchar(100),
    last_name         varchar(100),
    email             varchar(100),
    contact           varchar(50),
    user_confirmation varchar(200),
    totalPrice        decimal,
    total_amount      int8,
    payment_type      varchar(50),
    delivery_type     varchar(50),
    region            varchar(200),
    city              varchar(200),
    address           varchar(200),
    booking_items     text,
    PRIMARY KEY (id)
);

CREATE TABLE public.statistics
(
    id           serial8 NOT NULL,
    update_date  date,
    totalPrice   decimal,
    total_amount int8,
    PRIMARY KEY (id)
);



