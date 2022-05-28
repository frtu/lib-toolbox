CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS email
(
    id              uuid                        PRIMARY KEY     DEFAULT uuid_generate_v4(),
    receiver        VARCHAR(255)                NOT NULL,
    subject         VARCHAR(130)                NOT NULL,
    content         VARCHAR(255)                NOT NULL,
    status          VARCHAR(10)                 NOT NULL,
    creation_time   TIMESTAMP WITH TIME ZONE    NOT NULL        DEFAULT now(),
    update_time     TIMESTAMP WITH TIME ZONE    NOT NULL        DEFAULT now()
);