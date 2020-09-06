CREATE TABLE public.feed_back
(
    id             serial8 NOT NULL,
    user_name      varchar(100),
    userLastName   varchar(100),
    dateOfComment  varchar(100),
    recommendation boolean,
    mark           int4 NOT NULL,
    comment        text,
    PRIMARY KEY (id)
);



