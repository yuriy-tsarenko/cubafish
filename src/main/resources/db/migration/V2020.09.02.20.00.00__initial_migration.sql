CREATE TABLE public.dictionary
(
    id            serial8 NOT NULL,
    original_word varchar(100),
    uk_word       varchar(100),
    en_word       varchar(100),
    PRIMARY KEY (id)
);

