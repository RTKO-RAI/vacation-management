CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);
ALTER TABLE users ADD COLUMN project_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_users_project FOREIGN KEY (project_id) REFERENCES projects(id);
