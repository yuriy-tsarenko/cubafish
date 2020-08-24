ALTER TABLE public.booking_data_base
    ALTER COLUMN total_amount TYPE int4;

ALTER TABLE public.booking_list
    ADD COLUMN booking_comments text;

CREATE TABLE public.booking_item
(
    id          serial8 NOT NULL,
    product_key int4    NOT NULL,
    description   varchar(100),
    product_image_name varchar(255),
    totalPrice        decimal,
    total_amount      int4,
    item_amount      int4,
    PRIMARY KEY (id)
);

