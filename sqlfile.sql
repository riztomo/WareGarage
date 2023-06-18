--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: commodities; Type: TYPE; Schema: public; Owner: muhammad.rizky18
--

CREATE TYPE public.commodities AS ENUM (
    'LIVE',
    'FOOD',
    'EARTH',
    'ELECTRONIC',
    'PHARMA',
    'FURNITURE',
    'TRANSPORT'
);


ALTER TYPE public.commodities OWNER TO "muhammad.rizky18";

--
-- Name: employee_type; Type: TYPE; Schema: public; Owner: muhammad.rizky18
--

CREATE TYPE public.employee_type AS ENUM (
    'MANAGER',
    'OWNER'
);


ALTER TYPE public.employee_type OWNER TO "muhammad.rizky18";

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: commodity; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.commodity (
    id bigint NOT NULL,
    commodity_id text,
    section_id bigint NOT NULL,
    volume bigint NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.commodity OWNER TO "muhammad.rizky18";

--
-- Name: commodity_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.commodity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.commodity_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: commodity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.commodity_id_seq OWNED BY public.commodity.id;


--
-- Name: company; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.company (
    id bigint NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.company OWNER TO "muhammad.rizky18";

--
-- Name: company_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.company_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: company_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.company_id_seq OWNED BY public.company.id;


--
-- Name: employee; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.employee (
    id bigint NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    name text NOT NULL,
    company_id bigint NOT NULL,
    role public.employee_type NOT NULL
);


ALTER TABLE public.employee OWNER TO "muhammad.rizky18";

--
-- Name: employee_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.employee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.employee_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: employee_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.employee_id_seq OWNED BY public.employee.id;


--
-- Name: inout_records; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.inout_records (
    id bigint NOT NULL,
    company_id bigint NOT NULL,
    warehouse text NOT NULL,
    section text NOT NULL,
    commodity_id text NOT NULL,
    name text NOT NULL,
    type public.commodities NOT NULL,
    volume bigint NOT NULL,
    in_time date,
    out_time date
);


ALTER TABLE public.inout_records OWNER TO "muhammad.rizky18";

--
-- Name: inout_records_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.inout_records_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.inout_records_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: inout_records_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.inout_records_id_seq OWNED BY public.inout_records.id;


--
-- Name: section; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.section (
    id bigint NOT NULL,
    warehouse_id bigint NOT NULL,
    name text NOT NULL,
    commodity_type public.commodities NOT NULL,
    rem_capacity bigint NOT NULL
);


ALTER TABLE public.section OWNER TO "muhammad.rizky18";

--
-- Name: section_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.section_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: section_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.section_id_seq OWNED BY public.section.id;


--
-- Name: warehouse; Type: TABLE; Schema: public; Owner: muhammad.rizky18
--

CREATE TABLE public.warehouse (
    id bigint NOT NULL,
    address text NOT NULL,
    name text NOT NULL,
    company_id bigint NOT NULL,
    manager_id bigint,
    rem_capacity bigint NOT NULL
);


ALTER TABLE public.warehouse OWNER TO "muhammad.rizky18";

--
-- Name: warehouse_id_seq; Type: SEQUENCE; Schema: public; Owner: muhammad.rizky18
--

CREATE SEQUENCE public.warehouse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.warehouse_id_seq OWNER TO "muhammad.rizky18";

--
-- Name: warehouse_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: muhammad.rizky18
--

ALTER SEQUENCE public.warehouse_id_seq OWNED BY public.warehouse.id;


--
-- Name: commodity id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.commodity ALTER COLUMN id SET DEFAULT nextval('public.commodity_id_seq'::regclass);


--
-- Name: company id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.company ALTER COLUMN id SET DEFAULT nextval('public.company_id_seq'::regclass);


--
-- Name: employee id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.employee ALTER COLUMN id SET DEFAULT nextval('public.employee_id_seq'::regclass);


--
-- Name: inout_records id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.inout_records ALTER COLUMN id SET DEFAULT nextval('public.inout_records_id_seq'::regclass);


--
-- Name: section id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.section ALTER COLUMN id SET DEFAULT nextval('public.section_id_seq'::regclass);


--
-- Name: warehouse id; Type: DEFAULT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.warehouse ALTER COLUMN id SET DEFAULT nextval('public.warehouse_id_seq'::regclass);


--
-- Data for Name: commodity; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.commodity (id, commodity_id, section_id, volume, name) FROM stdin;
\.


--
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.company (id, name) FROM stdin;
15	Hello
16	Hello2
17	Hello3
18	Rizky3
\.


--
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.employee (id, username, password, name, company_id, role) FROM stdin;
2	coba123	$2b$08$Q1OoxDAKmsITOqi2ZsHxHey5e/QLF0FBXIi0HMPaA4HOuQ0GPhdPa	coba123	13	OWNER
4	rizky123	$2b$08$E5PA4z/09ZkSU9khDKL0buJl5tqtkF5ZPLul1B0AbUQwPf8F6aOyi	rizky123	16	OWNER
5	rizky1234	$2b$08$hAyDh2ONzyV/XUzzGMorZefQRthNjqykRGBJO3ebQxQofTQ3kT1yi	rizky1234	17	OWNER
6	riztomo	$2b$08$gwj0/WrX6wmA2a8X1fGiWeOn4RcC.m.ArO5oKu/SQYwqmjPSAoPkS	riztomo	18	OWNER
\.


--
-- Data for Name: inout_records; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.inout_records (id, company_id, warehouse, section, commodity_id, name, type, volume, in_time, out_time) FROM stdin;
\.


--
-- Data for Name: section; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.section (id, warehouse_id, name, commodity_type, rem_capacity) FROM stdin;
\.


--
-- Data for Name: warehouse; Type: TABLE DATA; Schema: public; Owner: muhammad.rizky18
--

COPY public.warehouse (id, address, name, company_id, manager_id, rem_capacity) FROM stdin;
2	theGrinch	rizkywares	18	\N	400
\.


--
-- Name: commodity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.commodity_id_seq', 1, false);


--
-- Name: company_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.company_id_seq', 18, true);


--
-- Name: employee_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.employee_id_seq', 6, true);


--
-- Name: inout_records_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.inout_records_id_seq', 1, false);


--
-- Name: section_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.section_id_seq', 1, false);


--
-- Name: warehouse_id_seq; Type: SEQUENCE SET; Schema: public; Owner: muhammad.rizky18
--

SELECT pg_catalog.setval('public.warehouse_id_seq', 2, true);


--
-- Name: commodity commodity_commodity_id_key; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.commodity
    ADD CONSTRAINT commodity_commodity_id_key UNIQUE (commodity_id);


--
-- Name: commodity commodity_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.commodity
    ADD CONSTRAINT commodity_pkey PRIMARY KEY (id);


--
-- Name: company company_name_key; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_name_key UNIQUE (name);


--
-- Name: company company_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (id);


--
-- Name: employee employee_username_key; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_username_key UNIQUE (username);


--
-- Name: inout_records inout_records_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.inout_records
    ADD CONSTRAINT inout_records_pkey PRIMARY KEY (id);


--
-- Name: section section_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.section
    ADD CONSTRAINT section_pkey PRIMARY KEY (id);


--
-- Name: warehouse warehouse_pkey; Type: CONSTRAINT; Schema: public; Owner: muhammad.rizky18
--

ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT warehouse_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

