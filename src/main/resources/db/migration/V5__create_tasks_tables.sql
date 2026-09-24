CREATE TABLE tasks(
    id               BIGSERIAL PRIMARY KEY,
    task_name        VARCHAR(255) NOT NULL,
    task_description VARCHAR(255) NOT NULL,
    task_status      VARCHAR(50)  NOT NULL,
    campaign_id      BIGINT       NOT NULL,
    client_id        BIGINT       NOT NULL,
    uploaded_by_name VARCHAR(255) NOT NULL,

    CONSTRAINT fk_tasks_campaign
        FOREIGN KEY (campaign_id)
            REFERENCES campaigns (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_tasks_client
        FOREIGN KEY (client_id)
            REFERENCES users (id)
);

CREATE TABLE task_images(
    task_id    BIGINT NOT NULL,
    image_path VARCHAR(255),

    CONSTRAINT fk_task_images_task
        FOREIGN KEY (task_id)
            REFERENCES tasks (id)
            ON DELETE CASCADE
);