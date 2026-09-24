CREATE TABLE campaigns
(
    id                   BIGSERIAL PRIMARY KEY,
    campaign_name        VARCHAR(255)   NOT NULL UNIQUE,
    campaign_description VARCHAR(255),
    price                NUMERIC(19, 2) NOT NULL
);

CREATE TABLE campaign_clients(
    campaign_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY (campaign_id, user_id),

    CONSTRAINT fk_campaign_clients_campaign
    FOREIGN KEY (campaign_id)
    REFERENCES campaigns(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_campaign_clients_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE campaign_employees (
    campaign_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY (campaign_id, user_id),
    CONSTRAINT fk_campaign_employees_campaign
        FOREIGN KEY (campaign_id)
        REFERENCES campaigns(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_campaign_employees_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);