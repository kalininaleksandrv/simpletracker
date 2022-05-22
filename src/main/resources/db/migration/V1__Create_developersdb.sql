CREATE TABLE IF NOT EXISTS developers
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name character varying(50),

    CONSTRAINT developers_pkey PRIMARY KEY (id)
);

INSERT INTO developers (name) VALUES ('John Smith');
INSERT INTO developers (name) VALUES ('Chi Miin');
INSERT INTO developers (name) VALUES ('Alexey Dostoyevsky');