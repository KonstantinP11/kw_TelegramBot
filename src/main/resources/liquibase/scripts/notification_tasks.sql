-- liquibase formatted sql

-- changeset perev:1
CREATE TABLE notification_task
(
    id UUID DEFAULT gen_random_uuid(),
    chat_id BIGINT NOT NULL,
    notification TEXT,
    alarm_date TIMESTAMP,
    CONSTRAINT notification_task_pk PRIMARY KEY (id)
);