CREATE TABLE IF NOT EXISTS issue
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    dtype varchar(50),
    date timestamp,
    title varchar(255),
    description varchar(255),
    issue_id varchar(50),
    issue_type varchar(50),
    bug_status varchar(50),
    bug_priority varchar(50),
    story_status varchar(50),
    points integer,
    developer_id bigint,
    foreign key (developer_id) references developer(id),
    CONSTRAINT issue_pkey PRIMARY KEY (id)
);