CREATE TABLE public.partner_apps
(
    id         uuid                         NOT NULL,
    chat_id    bigint                       NOT NULL,
    secondname character varying(50)        NOT NULL,
    firstname  character varying(50)        NOT NULL,
    thirdname  character varying(50),
    spiritname character varying(50),
    birthdate  date                         NOT NULL,
    phone      character varying(10)        NOT NULL,
    login_tg   character varying(40),
    email      character varying(60),
    app_date   date    DEFAULT CURRENT_DATE NOT NULL,
    unit_id    bigint,
    has_lesson boolean DEFAULT false        NOT NULL,
    CONSTRAINT partner_apps_pkey PRIMARY KEY (id)

);

CREATE TABLE public.partners
(
    id             uuid                         NOT NULL,
    chat_id        bigint                       NOT NULL,
    secondname     character varying(50)        NOT NULL,
    firstname      character varying(50)        NOT NULL,
    thirdname      character varying(50),
    spiritname     character varying(50),
    birthdate      date                         NOT NULL,
    phone          character varying(10)        NOT NULL,
    login_tg       character varying(40),
    email          character varying(60),
    status_id      bigint  DEFAULT 1            NOT NULL,
    employee_id    bigint,
    verified_phone character varying(10),
    reg_date       date    DEFAULT CURRENT_DATE NOT NULL,
    unit_id        bigint,
    has_lesson     boolean DEFAULT false        NOT NULL,
    CONSTRAINT partners_pkey PRIMARY KEY (id)
);
