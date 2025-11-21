CREATE TABLE teams (
    team_name VARCHAR(255) PRIMARY KEY
);

CREATE TABLE users (
    user_id    VARCHAR(255) PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    team_name  VARCHAR(255) NOT NULL,
    CONSTRAINT fk_users_team
        FOREIGN KEY (team_name) REFERENCES teams (team_name)
);

CREATE TABLE pull_requests (
    pull_request_id   VARCHAR(255) PRIMARY KEY,
    pull_request_name VARCHAR(255) NOT NULL,
    author_id         VARCHAR(255) NOT NULL,
    status            VARCHAR(16)  NOT NULL,
    created_at        TIMESTAMPTZ  NOT NULL DEFAULT now(),
    merged_at         TIMESTAMPTZ  NULL,
    CONSTRAINT fk_pr_author
        FOREIGN KEY (author_id) REFERENCES users (user_id),
    CONSTRAINT chk_pr_status
        CHECK (status IN ('OPEN', 'MERGED'))
);

CREATE TABLE pull_request_reviewers (
    pull_request_id VARCHAR(255) NOT NULL,
    reviewer_id     VARCHAR(255) NOT NULL,
    PRIMARY KEY (pull_request_id, reviewer_id),
    CONSTRAINT fk_pr_rev_pr
        FOREIGN KEY (pull_request_id) REFERENCES pull_requests (pull_request_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_pr_rev_user
        FOREIGN KEY (reviewer_id) REFERENCES users (user_id)
);

CREATE INDEX idx_pr_reviewers_reviewer_id
    ON pull_request_reviewers (reviewer_id);
