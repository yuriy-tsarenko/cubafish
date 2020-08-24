ALTER TABLE public.booking_item RENAME COLUMN total_price TO product_price;
ALTER TABLE public.booking_item ADD COLUMN creation_time VARCHAR(255);

