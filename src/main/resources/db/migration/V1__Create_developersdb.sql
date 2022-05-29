CREATE TABLE IF NOT EXISTS developer
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name character varying(50),

    CONSTRAINT developers_pkey PRIMARY KEY (id)
);

INSERT INTO developer (name) VALUES ('John Smith');
INSERT INTO developer (name) VALUES ('Chi Miin');
INSERT INTO developer (name) VALUES ('Alexey Dostoyevsky');