CREATE TABLE work_hours (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            project_id BIGINT NOT NULL,
                            total_worked_hours INT,
                            week_start_date DATE,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (project_id) REFERENCES projects(id)
);
