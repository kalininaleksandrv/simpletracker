CREATE TABLE IF NOT EXISTS developers
(
    id BIGINT NOT NULL PRIMARY KEY,
    name character varying(50),

    CONSTRAINT developers_pkey PRIMARY KEY (id)
);