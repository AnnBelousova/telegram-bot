-- liquibase formatted sql

-- changeSet annabelousova:1
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    notificationTask TEXT NOT NULL,
    taskTime TIMESTAMP NOT NULL
);