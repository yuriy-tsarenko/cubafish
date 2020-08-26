ALTER TABLE public.statistics ADD COLUMN booking_amount int8;
ALTER TABLE public.statistics ADD COLUMN canceled_booking_amount int8;
ALTER TABLE public.statistics ADD COLUMN canceled_booking_price decimal;
ALTER TABLE public.statistics ADD COLUMN reporting_period varchar(50);
ALTER TABLE public.statistics ALTER COLUMN update_date TYPE varchar(50);
ALTER TABLE public.booking_data_base ADD COLUMN date_of_saving_to_db varchar(50);

